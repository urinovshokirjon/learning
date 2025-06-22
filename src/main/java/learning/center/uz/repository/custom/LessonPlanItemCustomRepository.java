package learning.center.uz.repository.custom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import learning.center.uz.dto.lesson.LessonPlanDTO;
import learning.center.uz.dto.lesson.LessonPlanFilterDTO;
import learning.center.uz.dto.lesson.LessonPlanItemDTO;
import learning.center.uz.dto.lesson.LessonPlanItemFilterDTO;
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
public class LessonPlanItemCustomRepository {
    private final EntityManager entityManager;

    public PageImpl<LessonPlanItemDTO> filter(LessonPlanItemFilterDTO filter, AppLanguage appLanguage, int page, int size, String companyId) {
        StringBuilder builder = new StringBuilder(" AND lpi.visible = true ");
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("companyId", companyId);

        if (filter.getTitle() != null) {
            builder.append(" AND LOWER(lpi.title) LIKE :title ");
            params.put("title", "%" + filter.getTitle().toLowerCase() + "%");
        }
        if (filter.getDescription() != null) {
            builder.append(" AND LOWER(lpi.description) LIKE :description ");
            params.put("description", "%" + filter.getDescription().toLowerCase() + "%");
        }
        if (filter.getOrderNumber() != null){
            builder.append(" AND lpi.order_number LIKE :orderNumber ");
            params.put("orderNumber", "%" + filter.getOrderNumber() + "%");
        }
        if (filter.getHomework() != null){
            builder.append(" AND LOWER(lpi.homework) LIKE :homework ");
            params.put("homework", "%" + filter.getHomework().toLowerCase() + "%");
        }
        if (filter.getLessonPlanName() != null){
            builder.append(" AND LOWER(lp.name) LIKE :name ");
            params.put("name", "%" + filter.getLessonPlanName().toLowerCase() + "%");
        }


        // Join table
        StringBuilder joinLessonPlan = new StringBuilder(" LEFT JOIN lesson_plan AS lp ON lp.id = lpi.lesson_plan_id ");
        StringBuilder joinCompany = new StringBuilder(" LEFT JOIN company AS c ON c.id = lp.company_id WHERE c.id = :companyId ");

        // Select query
        StringBuilder selectBuilder = new StringBuilder(" SELECT " +
                " lpi.id, " +
                " lpi.title, " +
                " lpi.description, " +
                " lpi.order_number, " +
                " lpi.homework, " +
                " lpi.created_date, " +
                " lp.name " +
                " FROM lesson_plan_item AS lpi ")
                .append(joinLessonPlan)
                .append(joinCompany)
                .append(builder)
                .append(" ORDER BY lpi.created_date DESC ");

        // Count query
        StringBuilder countBuilder = new StringBuilder(" SELECT " +
                " count(lpi) FROM lesson_plan_item AS lpi ")
                .append(joinLessonPlan)
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
        Long totalElement = ((Long) countQuery.getSingleResult());

        List<LessonPlanItemDTO> dtoList = new LinkedList<>();
        for (Object[] object : entityList) {
            LessonPlanItemDTO dto = new LessonPlanItemDTO();
            dto.setId(MapperUtil.getStringValue(object[0]));
            dto.setTitle(MapperUtil.getStringValue(object[1]));
            dto.setDescription(MapperUtil.getStringValue(object[2]));
            dto.setOrderNumber(MapperUtil.getStringValue(object[3]));
            dto.setHomework(MapperUtil.getStringValue(object[4]));
            dto.setCreatedDate(MapperUtil.getLocalDateTimeValue(object[5]));
            dto.setLessonPlanName(MapperUtil.getStringValue(object[6]));
            dtoList.add(dto);
        }
        return new PageImpl<>(dtoList, PageRequest.of(page, size), totalElement);
    }
}

