package learning.center.uz.service;

import learning.center.uz.dto.lesson.LessonPlanDTO;
import learning.center.uz.dto.lesson.LessonPlanFilterDTO;
import learning.center.uz.dto.lesson.LessonPlanItemDTO;
import learning.center.uz.entity.LessonPlanEntity;
import learning.center.uz.enums.ProfileRole;
import learning.center.uz.exp.AppBadException;
import learning.center.uz.mapper.LessonPlanItemMapperI;
import learning.center.uz.mapper.LessonPlanMapperI;
import learning.center.uz.repository.LessonPlanRepository;
import learning.center.uz.repository.custom.LessonPlanCustomRepository;
import learning.center.uz.service.company.CompanyService;
import learning.center.uz.util.SpringSecurityUtil;
import lombok.RequiredArgsConstructor;
import org.hibernate.dialect.function.LpadRpadPadEmulation;
import org.springframework.data.domain.Page;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class LessonPlanService {

    private final LessonPlanRepository lessonPlanRepository;
    private final CompanyService companyService;
    private final LessonPlanCustomRepository lessonPlanCustomRepository;
    private final LessonPlanItemService lessonPlanItemService;


    public void create(LessonPlanDTO lessonPlanDTO) {
        Optional<LessonPlanEntity> optional = lessonPlanRepository.findByNameAndCompanyId(lessonPlanDTO.getLessonPlanName(), companyService.getCompanyIdByOwnerId(SpringSecurityUtil.getCurrentUserId()));
        if (optional.isPresent()) {
            throw new AppBadException("Already exist Lesson Plan!");
        }
        LessonPlanEntity lessonPlanEntity = new LessonPlanEntity();
        lessonPlanEntity.setName(lessonPlanDTO.getLessonPlanName());
        lessonPlanEntity.setSubjectId(lessonPlanDTO.getSubjectId());
        lessonPlanEntity.setCompanyId(companyService.getCompanyIdByOwnerId(SpringSecurityUtil.getCurrentUserId()));
        lessonPlanRepository.save(lessonPlanEntity);
    }


    public void update(String lessonPlanId, LessonPlanDTO lessonPlanDTO) {
        lessonPlanRepository.save(toEntity(lessonPlanId, lessonPlanDTO));
    }


    private LessonPlanEntity toEntity(String lessonPlanId, LessonPlanDTO lessonPlanDTO) {
        LessonPlanEntity entity = get(lessonPlanId);
        entity.setName(lessonPlanDTO.getLessonPlanName());
        entity.setSubjectId(lessonPlanDTO.getSubjectId());
        return entity;
    }


    public LessonPlanDTO getById(String id) {
        LessonPlanEntity lessonPlanEntity = get(id);
        return toDto(lessonPlanEntity);
    }


    public LessonPlanDTO getLessonPlanByLessonPlanItemId(String lessonPlanItemId) {
        Optional<LessonPlanEntity> optional = lessonPlanRepository.findByLessonPlanItem(lessonPlanItemId);
        if (optional.isEmpty()) {
            throw new AppBadException("Lesson Plan Item not found");
        }
        return toDto(optional.get());
    }


    public LessonPlanDTO toDto(LessonPlanEntity lessonPlanEntity) {
        LessonPlanDTO dto = new LessonPlanDTO();
        dto.setId(lessonPlanEntity.getId());
        dto.setLessonPlanName(lessonPlanEntity.getName());
        dto.setSubjectId(lessonPlanEntity.getSubjectId());
        dto.setSubjectName(lessonPlanEntity.getSubject().getName());
        return dto;
    }


    public LessonPlanEntity get(String id) {
        return lessonPlanRepository.findByIdAndVisibleTrue(id)
                .orElseThrow(() -> new AppBadException("Not found lessonPlan!"));
    }


    public boolean delete(String lessonPlanId) {
        int effectedRows = lessonPlanRepository.deleteById(lessonPlanId, SpringSecurityUtil.getCurrentUserId());
        return effectedRows != 0;
    }


    public Page<LessonPlanDTO> getLessonPlanList(LessonPlanFilterDTO filterDTO, int page, int size) {
        String companyId = companyService.getCompanyIdByOwnerId(SpringSecurityUtil.getCurrentUserId());
        Page<LessonPlanDTO> result = lessonPlanCustomRepository.filter(filterDTO, null, page - 1, size, companyId);
        return result;
    }


    public List<LessonPlanMapperI> getLessonPlanList() {
        return lessonPlanRepository.findByCompanyIdAndVisibleTrue(companyService.getCompanyIdByOwnerId(SpringSecurityUtil.getCurrentUserId()));
    }

    // LessonPlan list for create Group with Subject id
    // in dehqoncha: Group create qilishda Subject tanlaganda shu subjectga taalluqli LessonPlan lar listini olish
    public List<LessonPlanMapperI> getLessonPlanBySubjectId(String subjectId) {
        return lessonPlanRepository.getLessonPlanBySubjectId(subjectId);
    }

    public List<LessonPlanItemMapperI> getItemsByLessonPlanId(String lessonPlanId) {
        return lessonPlanItemService.getLessonPlanItemListByLessonPlanId(lessonPlanId);
    }
}
