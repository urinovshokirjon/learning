package learning.center.uz.service;

import learning.center.uz.dto.lesson.LessonPlanItemDTO;
import learning.center.uz.dto.lesson.LessonPlanItemFilterDTO;
import learning.center.uz.entity.LessonPlanItemEntity;
import learning.center.uz.exp.AppBadException;
import learning.center.uz.mapper.LessonPlanItemMapperI;
import learning.center.uz.repository.LessonPlanItemRepository;
import learning.center.uz.repository.custom.LessonPlanItemCustomRepository;
import learning.center.uz.service.company.CompanyService;
import learning.center.uz.util.SpringSecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class LessonPlanItemService {

    private final LessonPlanItemRepository lessonPlanItemRepository;
    private final CompanyService companyService;
    private final LessonPlanItemCustomRepository lessonPlanItemCustomRepository;


    public void create(LessonPlanItemDTO lpiDto, String lessonPlanId) {
        /*Optional<LessonPlanItemEntity> optional = lessonPlanItemRepository.findByOrderNumberOrTitle(lpiDto.getOrderNumber(), lpiDto.getTitle());
        if (optional.isPresent()) {
            throw new AppBadException("Already existing LessonPlanItem");
        }*/
        LessonPlanItemEntity lessonPlanItemEntity = new LessonPlanItemEntity();
        lessonPlanItemEntity.setTitle(lpiDto.getTitle());
        lessonPlanItemEntity.setDescription(lpiDto.getDescription());
        lessonPlanItemEntity.setOrderNumber(lpiDto.getOrderNumber());
        lessonPlanItemEntity.setHomework(lpiDto.getHomework());
        lessonPlanItemEntity.setLessonPlanId(lessonPlanId);
        lessonPlanItemRepository.save(lessonPlanItemEntity);
    }

    public void update(String lpiId, LessonPlanItemDTO lpiDto) {
        lessonPlanItemRepository.save(toEntity(lpiId, lpiDto));
    }

    public LessonPlanItemEntity toEntity(String lpiId, LessonPlanItemDTO lpiDto) {
        LessonPlanItemEntity entity = get(lpiId);
        entity.setTitle(lpiDto.getTitle());
        entity.setDescription(lpiDto.getDescription());
        entity.setOrderNumber(lpiDto.getOrderNumber());
        entity.setHomework(lpiDto.getHomework());
        entity.setUpdatedDate(LocalDateTime.now());
        return entity;
    }


    public LessonPlanItemEntity getByLessonPlanId(String lpId) {
        return lessonPlanItemRepository.findByLessonPlanIdAndVisibleTrue(lpId)
                .orElseThrow(() -> new AppBadException("Not found LessonPlanItem!"));
    }


    public LessonPlanItemDTO getByLPId(String id) {
        LessonPlanItemEntity entity = getByLessonPlanId(id);
        return toDto(entity);
    }


    public LessonPlanItemEntity get(String lpiId) {
        return lessonPlanItemRepository.findByIdAndVisibleTrue(lpiId)
                .orElseThrow(() -> new AppBadException("Not found LessonPlanItem!"));
    }

    public LessonPlanItemDTO getById(String id) {
        LessonPlanItemEntity entity = get(id);
        return toDto(entity);
    }

    public LessonPlanItemDTO toDto(LessonPlanItemEntity lessonPlanItemEntity) {
        LessonPlanItemDTO lessonPlanItemDTO = new LessonPlanItemDTO();
        lessonPlanItemDTO.setId(lessonPlanItemEntity.getId());
        lessonPlanItemDTO.setTitle(lessonPlanItemEntity.getTitle());
        lessonPlanItemDTO.setDescription(lessonPlanItemEntity.getDescription());
        lessonPlanItemDTO.setOrderNumber(lessonPlanItemEntity.getOrderNumber());
        lessonPlanItemDTO.setHomework(lessonPlanItemEntity.getHomework());
        lessonPlanItemDTO.setLessonPlanId(lessonPlanItemEntity.getLessonPlanId());
        lessonPlanItemDTO.setCreatedDate(lessonPlanItemEntity.getCreatedDate());
        return lessonPlanItemDTO;
    }

    public boolean delete(String lpiId) {
        int effectedRows = lessonPlanItemRepository.deleteById(SpringSecurityUtil.getCurrentUserId(), lpiId);
        return effectedRows != 0;
    }


    public Page<LessonPlanItemDTO> getLessonPlanItemList(LessonPlanItemFilterDTO filterDTO, int page, int size) {
        String companyId = companyService.getCompanyIdByOwnerId(SpringSecurityUtil.getCurrentUserId());
        Page<LessonPlanItemDTO> result = lessonPlanItemCustomRepository.filter(filterDTO, null, page - 1, size, companyId);
        return result;
    }

    public List<LessonPlanItemMapperI> getLessonPlanItemListByLessonPlanId(String lessonPlanId) {
        return lessonPlanItemRepository.getLessonPlanItemListByLessonPlanId(lessonPlanId);
    }
}
