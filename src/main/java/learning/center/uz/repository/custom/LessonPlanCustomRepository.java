package learning.center.uz.repository.custom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import learning.center.uz.dto.lesson.LessonPlanDTO;
import learning.center.uz.dto.lesson.LessonPlanFilterDTO;
import learning.center.uz.enums.AppLanguage;
import learning.center.uz.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class LessonPlanCustomRepository {
    private final EntityManager entityManager;

    public PageImpl<LessonPlanDTO> filter(LessonPlanFilterDTO filter, AppLanguage appLanguage, int page, int size, String companyId) {
        StringBuilder builder = new StringBuilder(" AND lp.visible=true ");
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("companyId", companyId);

        if (filter.getLessonPlanName() != null) {
            builder.append(" AND LOWER(lp.name) LIKE :name ");
            params.put("name", "%" + filter.getLessonPlanName().toLowerCase() + "%");
        }
        if (filter.getSubjectName() != null) {
            builder.append(" AND LOWER(s.name) LIKE :subject ");
            params.put("subject", "%" + filter.getSubjectName().toLowerCase() + "%");
        }
        // Join table
        StringBuilder joinSubject = new StringBuilder(" LEFT JOIN subject AS s ON s.id = lp.subject_id ");
        StringBuilder joinCompany = new StringBuilder(" LEFT JOIN company AS c ON c.id = lp.company_id WHERE c.id = :companyId ");

        // Select query
        StringBuilder selectBuilder = new StringBuilder(" SELECT " +
                " lp.id, " +
                " lp.name, " +
                " lp.created_date, " +
                " s.name " +
                " FROM lesson_plan AS lp ")
                .append(joinSubject)
                .append(joinCompany)
                .append(builder)
                .append(" ORDER BY lp.created_date DESC ");

        // Count query
        StringBuilder countBuilder = new StringBuilder(" SELECT " +
                " count(lp) FROM lesson_plan AS lp ")
                .append(joinSubject)
                .append(joinCompany)
                .append(builder);

        Query selectQuery = entityManager.createNativeQuery(selectBuilder.toString());
        Query countQuery = entityManager.createNativeQuery(countBuilder.toString());

        // Set parameters
        for (Map.Entry<String, Object> p : params.entrySet()) {
            selectQuery.setParameter(p.getKey(), p.getValue());
            countQuery.setParameter(p.getKey(), p.getValue());
        }

        // Set pagination
        selectQuery.setFirstResult(page * size);
        selectQuery.setMaxResults(size);

        // Get result
        List<Object[]> entityList = selectQuery.getResultList();
        Long totalElement = ((Number) countQuery.getSingleResult()).longValue();

        List<LessonPlanDTO> dtoList = new LinkedList<>();
        for (Object[] object : entityList) {
            LessonPlanDTO dto = new LessonPlanDTO();
            dto.setId(MapperUtil.getStringValue(object[0]));
            dto.setLessonPlanName(MapperUtil.getStringValue(object[1]));
            dto.setCreatedDate(MapperUtil.getLocalDateTimeValue(object[2]));
            dto.setSubjectName(MapperUtil.getStringValue(object[3]));
            dtoList.add(dto);
        }
        return new PageImpl<>(dtoList, PageRequest.of(page, size), totalElement);
    }
}

