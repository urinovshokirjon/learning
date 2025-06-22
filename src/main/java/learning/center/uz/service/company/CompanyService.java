package learning.center.uz.service.company;

import learning.center.uz.dto.admin.company.CompanyDTO;
import learning.center.uz.dto.admin.company.CompanyFilterDTO;
import learning.center.uz.dto.admin.profile.ProfileDTO;
import learning.center.uz.entity.company.CompanyEntity;
import learning.center.uz.exp.AppBadException;
import learning.center.uz.repository.company.CompanyRepository;
import learning.center.uz.repository.custom.CompanyCustomRepository;
import learning.center.uz.service.AttachService;
import learning.center.uz.service.ProfileService;
import learning.center.uz.util.SpringSecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


@Lazy
@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final ProfileService profileService;
    private final AttachService attachService;
    private final CompanyCustomRepository companyCustomRepository;

    public List<CompanyDTO> getAll() {
        Iterable<CompanyEntity> entityIterable = companyRepository.getAll();
        List<CompanyDTO> dtoList = new LinkedList<>();
        for (CompanyEntity companyEntity : entityIterable) {
            dtoList.add(toDTO(companyEntity));
        }
        return dtoList;
    }

    public CompanyDTO toDTO(CompanyEntity entity) {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setId(entity.getId());
        companyDTO.setContact(entity.getContact());
        if (entity.getPhotoId() != null) {
            companyDTO.setPhotoUrl(attachService.getOnlyUrl(entity.getPhotoId()));
        }
        companyDTO.setName(entity.getName());
        companyDTO.setCreatedDate(entity.getCreatedDate());
        companyDTO.setOwnerId(entity.getOwnerId());
        ProfileDTO profile = profileService.getProfileById(entity.getOwnerId());
        companyDTO.setOwner(profile);
        return companyDTO;
    }

    public void create(CompanyDTO dto, MultipartFile file) {
        String photoId = null;
        if (!file.isEmpty()) {
            photoId = attachService.save2(file);
        }
        CompanyEntity entity = new CompanyEntity();
        entity.setName(dto.getName());
        entity.setContact(dto.getContact());
        entity.setPhotoId(photoId);
        entity.setOwnerId(dto.getOwnerId());
        companyRepository.save(entity);
    }

    public List<ProfileDTO> getByCompanyProfiles() {
        return profileService.getRoleCompany();
    }

    public CompanyDTO getById(String id) {
        Optional<CompanyEntity> companyEntityOptional = companyRepository.findById(id);
        return companyEntityOptional.map(this::toDTO).orElse(null);
    }

    public void update(String companyId, CompanyDTO companyDTO, MultipartFile file) {
        Optional<CompanyEntity> optional = companyRepository.getById(companyId);
        CompanyEntity companyEntity = optional.get();

        if (!file.isEmpty()) {
            String photoId = attachService.save2(file);
            companyEntity.setPhotoId(photoId);
        }

        companyEntity.setName(companyDTO.getName());
        companyEntity.setContact(companyDTO.getContact());
        companyEntity.setOwnerId(companyDTO.getOwnerId());
        companyRepository.save(companyEntity);
    }

    public boolean delete(String id) {
        int effectedRows = companyRepository.deleteById(id, SpringSecurityUtil.getCurrentUserId());
        return effectedRows != 0;
    }

    public Page<CompanyDTO> getCompanyList(CompanyFilterDTO filterDTO, int page, int size) {
        return companyCustomRepository.filter(filterDTO, null, page - 1, size);
    }

    /*
     * MAZGILAR BU METHODGA ROLE_COMPANY MUROJAT QILADI.  ILTIMOS QO'YLANMANGLAR
     */
    public String getCompanyIdByOwnerId(String ownerId) {
        return companyRepository.getCompanyIdByOwnerId(ownerId);
    }

    /*
     * MAZGILAR BU METHODGA ROLE_COMPANY DAN BOSHQA ROLLAR MUROJAT QILADI.  ILTIMOS QO'YLANMANGLAR
     */
  /*  public String getCompanyIdByProfileId(String profileId) {
        return companyRepository.getCompanyIdByProfileId(profileId);
    }*/
}
