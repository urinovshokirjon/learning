package learning.center.uz.service;

import learning.center.uz.config.CustomUserDetails;
import learning.center.uz.dto.admin.profile.ProfileDTO;
import learning.center.uz.dto.admin.profile.ProfileFilterDTO;
import learning.center.uz.dto.admin.profile.ProfileRequest;
import learning.center.uz.entity.ProfileEntity;
import learning.center.uz.enums.ProfileRole;
import learning.center.uz.enums.ProfileStatus;
import learning.center.uz.exp.AppBadException;
import learning.center.uz.repository.BranchRepository;
import learning.center.uz.repository.ProfileRepository;
//import learning.center.uz.repository.company.CompanyProfileRepository;
import learning.center.uz.repository.company.CompanyRepository;
import learning.center.uz.repository.custom.CompanyProfileCustomRepository;
import learning.center.uz.repository.custom.ProfileCustomRepository;
import learning.center.uz.service.company.ProfileSubjectService;
import learning.center.uz.util.MD5Util;
import learning.center.uz.util.SpringSecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final ProfileRoleService profileRoleService;
    private final AttachService attachService;
    private final ProfileCustomRepository profileCustomRepository;
    private final CompanyRepository companyRepository;
    //    private final CompanyProfileRepository companyProfileRepository;
    private final ProfileSubjectService profileSubjectService;
    private final BranchRepository branchRepository;
    private final CompanyProfileCustomRepository companyProfileCustomRepository;

    public void create(ProfileRequest dto, MultipartFile file) {
        Optional<ProfileEntity> optional = profileRepository.findByPhone(dto.getPhone());
        if (optional.isPresent()) {
            throw new AppBadException("phone already in use");
        }

        String photoId = null;
        if (!file.isEmpty()) {
            photoId = attachService.save2(file);
        }
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setName(dto.getName());
        profileEntity.setSurname(dto.getSurname());
        profileEntity.setPhone(dto.getPhone());
        profileEntity.setStatus(ProfileStatus.ACTIVE);
        profileEntity.setPassword(MD5Util.encode(dto.getPassword()));
        profileEntity.setPhotoId(photoId);
        // set company ID
        String companyId = getRequestedProfileCompanyId();
        profileEntity.setCompanyId(companyId);

        profileRepository.save(profileEntity);
        profileRoleService.merge(profileEntity.getId(), dto.getRoles());
    }

    public void update(String id, ProfileRequest dto, MultipartFile file) {
        ProfileEntity profileEntity = profileRepository.getProfileById(id)
                .orElseThrow(() -> new AppBadException("profile not found"));

        if (!file.isEmpty()) {
            String photoId = attachService.save2(file);
            profileEntity.setPhotoId(photoId);
        }

        profileEntity.setName(dto.getName());
        profileEntity.setSurname(dto.getSurname());
        profileEntity.setPhone(dto.getPhone());
        profileRepository.save(profileEntity); // update
        profileRoleService.merge(profileEntity.getId(), dto.getRoles()); // update role list

        /*
         * Update qilinayotgan user role TEACHER bolsa shu userga biriktirilgan subjectlarni ham update qilinadi
         * Agar user role MANAGER bolsa oldin shu userga biriktirilgan subjectlarni remove qilinadi
         */
        if (dto.getRoles().contains(ProfileRole.ROLE_TEACHER) || dto.getRoles().contains(ProfileRole.ROLE_COMPANY_MANAGER)) {
            profileSubjectService.merge(profileEntity.getId(), dto.getSubject());
        }
    }

    /**
     * ADMIN
     */
    public Page<ProfileDTO> getProfileList(ProfileFilterDTO filterDTO, int page, int size) {
        List<SimpleGrantedAuthority> roleList = SpringSecurityUtil.getCurrentUserDetail().getRoleList();
        if (roleList.stream().anyMatch(role -> role.getAuthority().equals(ProfileRole.ROLE_ADMIN.toString()))) {
            return profileCustomRepository.filter(filterDTO, null, page, size);
        } else if (roleList.stream().anyMatch(role -> role.getAuthority().equals(ProfileRole.ROLE_COMPANY_MANAGER.toString()))) {
            return getCompanyProfileList(filterDTO, page, size);
        } else if (roleList.stream().anyMatch(role -> role.getAuthority().equals(ProfileRole.ROLE_BRANCH_MANAGER.toString()))) {
            return getBranchProfileList(filterDTO, page, size);
        }
        return null;
    }

    /**
     * COMPANY
     */
    public Page<ProfileDTO> getCompanyProfileList(ProfileFilterDTO filterDTO, int page, int size) {
        String companyId = companyRepository.getCompanyIdByOwnerId(SpringSecurityUtil.getCurrentUserId());
        Page<ProfileDTO> result = companyProfileCustomRepository.filter(filterDTO, null, page, size, null, companyId);
        return result;
    }

    /**
     * BRANCH
     */
    public Page<ProfileDTO> getBranchProfileList(ProfileFilterDTO filterDTO, int page, int size) {
        String branchId = branchRepository.getBranchIdByManagerId(SpringSecurityUtil.getCurrentUserId());
        Page<ProfileDTO> result = companyProfileCustomRepository.filter(filterDTO, null, page, size, branchId, null);
        return result;
    }


    public boolean delete(String id) {
        int effectedRows = profileRepository.deleteById(id, SpringSecurityUtil.getCurrentUserId());
        return effectedRows != 0;
    }

    public ProfileDTO getProfileById(String id) {
        Optional<ProfileEntity> optional = profileRepository.getProfileById(id);
        return toDTO(optional.get());
    }

    public ProfileDTO toDTO(ProfileEntity entity) {
        ProfileDTO dto = new ProfileDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setPhone(entity.getPhone());
        if (entity.getPhotoId() != null) {
            dto.setPhotoUrl(attachService.getOnlyUrl(entity.getPhotoId()));
        }
        dto.setRoles(profileRoleService.getProfileRoles(entity.getId()));
        dto.setSubject(profileSubjectService.getProfileSubjectIds(entity.getId()));
        dto.setStatus(entity.getStatus());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public List<ProfileDTO> getRoleCompany() {
        List<ProfileDTO> dtoList = new LinkedList<>();
        List<ProfileEntity> roleCompany = profileRepository.getRoleCompany();
        for (ProfileEntity profileEntity : roleCompany) {
            dtoList.add(toDTO(profileEntity));
        }
        return dtoList;
    }

    public List<ProfileDTO> getTeacherList() {
        String companyId = null;
        List<SimpleGrantedAuthority> roleList = SpringSecurityUtil.getCurrentUserDetail().getRoleList();
        if (roleList.stream().anyMatch(role -> role.getAuthority().equals(ProfileRole.ROLE_COMPANY_MANAGER.toString()))) {
            companyId = companyRepository.getCompanyIdByOwnerId(SpringSecurityUtil.getCurrentUserId());
        } else {
            companyId = branchRepository.getBranchIdByManagerId(SpringSecurityUtil.getCurrentUserId());
        }
        List<ProfileEntity> entityList = profileRepository.getTeacherList(companyId);

        return entityList.stream()
                .map(this::toDTO)
                .toList();
    }

    public String getCompanyIdByOwnerId() {
        return companyRepository.getCompanyIdByOwnerId(SpringSecurityUtil.getCurrentUserId());
    }

    public String getRequestedProfileCompanyId() {// murojat qilgan odamni companyId-ni aniqlash
        List<SimpleGrantedAuthority> roleList = SpringSecurityUtil.getCurrentUserDetail().getRoleList();
        if (roleList.stream().anyMatch(role -> ProfileRole.adminRoles.contains(role.getAuthority()))) {
            return null;  // ROLE_ADMIN and ROLE_MODERATOR do not have companyID
        } else if (roleList.stream().anyMatch(role -> role.getAuthority().equals(ProfileRole.ROLE_COMPANY_MANAGER.toString()))) {
            return companyRepository.getCompanyIdByOwnerId(SpringSecurityUtil.getCurrentUserId());
        } else if (roleList.stream()
                .anyMatch(role -> ProfileRole.companyRoles.contains(role.getAuthority()))) {
            return SpringSecurityUtil.getCurrentUserDetail().getCompanyId();
        }
        throw new RuntimeException("Profile Company not found");
    }

    public List<ProfileDTO> getRoleCompanyManager() {
        String companyId = getCompanyIdByOwnerId();
        List<ProfileEntity> entityList = profileRepository.getRoleCompanyManager(companyId);

        return entityList.stream()
                .map(this::toDTO)
                .toList();
    }

    public String getCurrenUserPhoto() {
        return SpringSecurityUtil.getCurrenUserPhoto();
    }

    public String getCurrenUserNameSurname() {
        CustomUserDetails customUserDetails = SpringSecurityUtil.getCurrentUserDetail();
        return customUserDetails != null ? customUserDetails.getName() + "\n" + customUserDetails.getSurname() : " ";
    }

    public CustomUserDetails getCurrenDetail() {
        return SpringSecurityUtil.getCurrentUserDetail();
    }

}
