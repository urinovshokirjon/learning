package learning.center.uz.repository.custom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import learning.center.uz.dto.student.StudentFilterDTO;
import learning.center.uz.dto.student.StudentResponse;
import learning.center.uz.enums.Gender;
import learning.center.uz.enums.StudentStatus;
import learning.center.uz.enums.StudyStatus;
import learning.center.uz.service.AttachService;
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
public class StudentCustomRepository {

    private final EntityManager entityManager;
    private final AttachService attachService;

    public PageImpl<StudentResponse> filter(StudentFilterDTO filter, int page, int size, String companyId, String branchId) {
        StringBuilder builder = new StringBuilder(" where s.visible = true ");
        Map<String, Object> params = new LinkedHashMap<>();

        if (companyId != null) { //g.branchId in (1,2,3,4)
            builder.append(" AND s.branch_id in (select b.id from branch as b where b.company_id = :companyId)");
            params.put("companyId", companyId);
        }

        if (branchId != null) {
            builder.append(" AND s.branch_id = :branchId");
            params.put("branchId", branchId);
        }

        if (filter.getNameQuery() != null) {
            builder.append(" and (lower(s.name) like :nameQuery or lower(s.surname) like :nameQuery) ");
            params.put("nameQuery", "%" + filter.getNameQuery().toLowerCase() + "%");
        }
        if (filter.getPhone() != null) {
            builder.append(" and s.phone like :phone ");
            params.put("phone", "%" + filter.getPhone() + "%");
        }
        if (filter.getGender() != null && !filter.getGender().equals("NON")) {
            builder.append(" and s.study_status = :study_status ");
            params.put("study_status", filter.getStudyStatus());
        }
        if (filter.getGender() != null && !filter.getGender().equals("NON")) {
            builder.append(" and s.gender = :gender ");
            params.put("gender", filter.getGender());
        }

        // Select query
        StringBuilder selectBuilder = new StringBuilder(
                " select s.id, s.name, s.surname, " +
                        " s.phone, s.parent_phone, s.photo_id, " +
                        " s.address, s.date_of_birth, s.gender, " +
                        " s.study_status, s.student_status, " +
                        " s.created_date from student as s "
        );

        // Count query
        StringBuilder countBuilder = new StringBuilder(" select count(s.id) from student as s ");


        selectBuilder.append(builder).append(" order by s.created_date desc ");
        countBuilder.append(builder);

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

        // Get results
        List<Object[]> entityList = selectQuery.getResultList();
        Long totalElement = MapperUtil.getLongValue(countQuery.getSingleResult());


        List<StudentResponse> dtoList = new LinkedList<>();
        for (Object[] object : entityList) {
            StudentResponse dto = new StudentResponse();
            dto.setId(MapperUtil.getStringValue(object[0]));
            dto.setName(MapperUtil.getStringValue(object[1]));
            dto.setSurname(MapperUtil.getStringValue(object[2]));
            dto.setPhone(MapperUtil.getStringValue(object[3]));
            dto.setParentPhone(MapperUtil.getStringValue(object[4]));
            if (object[5] != null) {
                dto.setPhotoUrl(attachService.getOnlyUrl((String) object[5]));
            }
            dto.setAddress(MapperUtil.getStringValue(object[6]));
            dto.setDateOfBirth(MapperUtil.getLocalDateValue(object[7]));
            dto.setGender(Gender.valueOf(MapperUtil.getStringValue(object[8])));
            dto.setStudyStatus(StudyStatus.valueOf(MapperUtil.getStringValue(object[9])));
            dto.setStudentStatus(StudentStatus.valueOf(MapperUtil.getStringValue(object[10])));
            dto.setCreatedDate(MapperUtil.getLocalDateTimeValue(object[11]));
            dtoList.add(dto);
        }
        return new PageImpl<>(dtoList, PageRequest.of(page, size), totalElement);
    }
}
