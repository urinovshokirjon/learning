package learning.center.uz.service;

import learning.center.uz.dto.branch.BranchDTO;
import learning.center.uz.dto.branch.BranchFilterDTO;
import learning.center.uz.entity.BranchEntity;
import learning.center.uz.exp.AppBadException;
import learning.center.uz.repository.BranchRepository;
import learning.center.uz.repository.custom.BranchCustomRepository;
import learning.center.uz.service.company.CompanyService;
import learning.center.uz.util.SpringSecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BranchService {

    private final AttachService attachService;
    private final BranchRepository branchRepository;
    private final CompanyService companyService;
    private final ProfileService profileService;
    private final BranchCustomRepository branchCustomRepository;

    public void create(BranchDTO dto, MultipartFile file) {
        Optional<BranchEntity> optional = branchRepository.findByNameAndCompanyId(dto.getBranchName(), companyService.getCompanyIdByOwnerId(SpringSecurityUtil.getCurrentUserId()));
        if (optional.isPresent()) {
            throw new AppBadException("Already exists a branch with name: " + dto.getBranchName());
        }
        BranchEntity branchEntity = new BranchEntity();
        branchEntity.setName(dto.getBranchName());
        branchEntity.setAddress(dto.getBranchAddress());
        branchEntity.setContact(dto.getContact());
        branchEntity.setCompanyId(companyService.getCompanyIdByOwnerId(SpringSecurityUtil.getCurrentUserId()));
        branchEntity.setManagerId(dto.getManagerId());
        String photoId = null;
        if (!file.isEmpty()) {
            photoId = attachService.save2(file);
        }
        branchEntity.setPhotoId(photoId);
        branchRepository.save(branchEntity);
    }


    public void update(String branchId, BranchDTO dto, MultipartFile file) {
        branchRepository.save(toEntity(branchId, dto, file));
    }


    public BranchDTO getBranchById(String id) {
        BranchEntity branchEntity = get(id);
        return toDto(branchEntity);
    }

    public BranchDTO toDto(BranchEntity branchEntity) {
        BranchDTO dto = new BranchDTO();
        dto.setId(branchEntity.getId());
        dto.setManagerId(branchEntity.getManagerId());
        dto.setBranchName(branchEntity.getName());
        dto.setBranchAddress(branchEntity.getAddress());
        dto.setManager(profileService.getProfileById(branchEntity.getManagerId()));
        dto.setContact(branchEntity.getContact());
        if (branchEntity.getPhotoId() != null) {
            dto.setPhotoUrl(attachService.getOnlyUrl(branchEntity.getPhotoId()));
        }
        return dto;
    }

    //Update
    public BranchEntity toEntity(String branchId, BranchDTO dto, MultipartFile file) {
        BranchEntity branchEntity = get(branchId);
        branchEntity.setName(dto.getBranchName());
        branchEntity.setAddress(dto.getBranchAddress());
        branchEntity.setContact(dto.getContact());
        branchEntity.setManagerId(dto.getManagerId());
        if (!file.isEmpty()) {
            String photoId = attachService.save2(file);
            branchEntity.setPhotoId(photoId);
        }
        return branchEntity;
    }

    public BranchEntity get(String branchId) {
        return branchRepository.findByIdAndVisibleTrue(branchId)
                .orElseThrow(() -> new AppBadException("Not found branch!"));
    }

    public boolean delete(String branchId) {
        int effectedRows = branchRepository.deleteById(branchId, SpringSecurityUtil.getCurrentUserId());
        return effectedRows != 0;
    }


    public Page<BranchDTO> getBranchList(BranchFilterDTO branchFilterDTO, int page, int size) {
        String companyId = companyService.getCompanyIdByOwnerId(SpringSecurityUtil.getCurrentUserId());
        Page<BranchDTO> result = branchCustomRepository.filter(branchFilterDTO, null, page - 1, size, companyId);
        return result;
    }

    public List<BranchDTO> getBranchListByCompanyId() {
        String companyId = companyService.getCompanyIdByOwnerId(SpringSecurityUtil.getCurrentUserId());
        List<BranchEntity> entityList = branchRepository.findAllByCompanyId(companyId);
        return entityList.stream().map(this::toDto).toList();
    }
}
