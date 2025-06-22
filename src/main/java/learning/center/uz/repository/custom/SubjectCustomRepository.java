package learning.center.uz.repository.custom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import learning.center.uz.dto.subject.SubjectDTO;
import learning.center.uz.dto.subject.SubjectFilterDTO;
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
public class SubjectCustomRepository {

    private final EntityManager entityManager;

    public PageImpl<SubjectDTO> filter(SubjectFilterDTO filter, String companyId, int page, int size) {
        StringBuilder builder = new StringBuilder(" and s.visible=true ");
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("companyId", companyId);

        if (filter.getNameQuery() != null) {
            builder.append(" and lower(s.name) like :queryParam ");
            params.put("queryParam", "%" + filter.getNameQuery().toLowerCase() + "%");
        }
        if (filter.getName() != null) {
            builder.append(" and s.name = :name ");
            params.put("name", filter.getName());
        }

        // SELECT so'rovi
        StringBuilder selectBuilder = new StringBuilder("SELECT " +
                " s.id, " +
                " s.name, " +
                " s.created_date, " +
                " COALESCE(gr.count, 0) " +
                " FROM subject AS s " +
                " LEFT JOIN (SELECT COUNT(*), subject_id FROM groups GROUP BY subject_id) AS gr ON gr.subject_id = s.id " +
                " WHERE s.company_id = :companyId ")
                .append(builder)
                .append(" ORDER BY s.created_date DESC ");

        // COUNT so'rovi
        StringBuilder countBuilder = new StringBuilder("SELECT " +
                " COUNT(s.id) FROM subject AS s " +
                " WHERE s.company_id = :companyId ")
                .append(builder);

        Query selectQuery = entityManager.createNativeQuery(selectBuilder.toString());
        Query countQuery = entityManager.createNativeQuery(countBuilder.toString());

        // Parametrlarni sozlash
        for (Map.Entry<String, Object> p : params.entrySet()) {
            selectQuery.setParameter(p.getKey(), p.getValue());
            countQuery.setParameter(p.getKey(), p.getValue());
        }

        // Paginatsiya
        selectQuery.setFirstResult(page * size);
        selectQuery.setMaxResults(size);

        // Natijalarni olish
        List<Object[]> entityList = selectQuery.getResultList();
        Long totalElement = ((Number) countQuery.getSingleResult()).longValue();

        List<SubjectDTO> dtoList = new LinkedList<>();
        for (Object[] object : entityList) {
            SubjectDTO dto = new SubjectDTO();
            dto.setId(MapperUtil.getStringValue(object[0]));
            dto.setName(MapperUtil.getStringValue(object[1]));
            dto.setCreatedDate(MapperUtil.getLocalDateTimeValue(object[2]));
            dto.setGroupCount(((Number) object[3]).intValue());
            dtoList.add(dto);
        }

        return new PageImpl<>(dtoList, PageRequest.of(page, size), totalElement);
    }
}

