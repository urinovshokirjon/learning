package learning.center.uz.repository.custom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import learning.center.uz.dto.group.GroupDTO;
import learning.center.uz.dto.group.GroupFilterDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class GroupCustomRepository {
    private final EntityManager entityManager;

    public PageImpl<GroupDTO> filter(GroupFilterDTO filter, int page, int size, String companyId, String branchId, String teacherId) {
        StringBuilder query = new StringBuilder(" where g.visible=true ");
        Map<String, Object> params = new LinkedHashMap<>();

        if (companyId != null) {
            query.append(" AND g.branch_id in (select b.id from branch as b where b.company_id = :companyId)");
            params.put("companyId", companyId);
        }

        if (branchId != null) {
            query.append(" AND g.branch_id = :branchId");
            params.put("branchId", branchId);
        }

        if (teacherId != null) {
            query.append(" AND g.teacher_id = :teacherId");
            params.put("teacherId", teacherId);
        }

        if (filter.getName() != null) {
            query.append(" AND lower(g.name) like :name ");
            params.put("name", "%" + filter.getName() + "%");
        }

        if (filter.getSubject() != null) {
            query.append(" AND lower(s.name) like :subject ");
            params.put("subject", "%" + filter.getSubject() + "%");
        }

        if (filter.getTeacher() != null) {
            query.append(" AND lower(p.name) like :teacher ");
            params.put("teacher", "%" + filter.getTeacher() + "%");
        }

        // select query
        StringBuilder selectSQL = new StringBuilder(
                " SELECT " +
                        " g.id, " +
                        " g.name, " +
                        " g.start_date, " +
                        " g.finished_date, " +
                        " g.created_date, " +
                        " s.name AS subject," +
                        " p.name AS teacher, " +
                        " (SELECT COUNT(*) FROM student_group sg WHERE sg.group_id = g.id) AS studentCount " +
                        " FROM groups AS g " +
                        " INNER JOIN subject AS s ON g.subject_id = s.id " +
                        " INNER JOIN profile AS p ON g.teacher_id = p.id "
        );

        // count query
        StringBuilder countSQL = new StringBuilder("SELECT count(*) " +
                " FROM groups AS g " +
                " INNER JOIN subject AS s ON g.subject_id = s.id " +
                " INNER JOIN profile AS p ON g.teacher_id = p.id ");

        selectSQL.append(query);
        countSQL.append(query);

        // Create and execute queries
        Query selectQuery = entityManager.createNativeQuery(selectSQL.toString());
        Query countQuery = entityManager.createNativeQuery(countSQL.toString());

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

        List<GroupDTO> dtoList = new LinkedList<>();
        for (Object[] object : entityList) {
            GroupDTO dto = new GroupDTO();
            dto.setId((String) object[0]);
            dto.setName((String) object[1]);
            dto.setStartDate(((java.sql.Date) object[2]).toLocalDate());
            if (object[3] != null){
                dto.setFinishedDate(((java.sql.Date) object[3]).toLocalDate());
            }
            dto.setCreatedDate(((java.sql.Timestamp) object[4]).toLocalDateTime().toLocalDate());
            dto.setSubjectName((String) object[5]);
            dto.setTeacherName((String) object[6]);
            dto.setStudentCount(((Long) object[7]));
            dtoList.add(dto);
        }

        return new PageImpl<>(dtoList, PageRequest.of(page, size), totalElement);
    }
}
