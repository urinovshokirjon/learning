package learning.center.uz.repository.custom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import learning.center.uz.dto.admin.profile.ProfileDTO;
import learning.center.uz.dto.admin.profile.ProfileFilterDTO;
import learning.center.uz.enums.AppLanguage;
import learning.center.uz.enums.ProfileStatus;
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
public class CompanyProfileCustomRepository {
    private final EntityManager entityManager;
    private final AttachService attachService;

    public PageImpl<ProfileDTO> filter(ProfileFilterDTO filter,
                                       AppLanguage appLanguage, int page, int size,
                                       String branchId, String companyId) {
        StringBuilder builder = new StringBuilder(" and p.visible=true ");
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("companyId", companyId);

        if (filter.getNameQuery() != null) {
            builder.append(" and (lower(p.name) like :queryParam or lower(p.surname) like :queryParam) ");
            params.put("queryParam", "%" + filter.getNameQuery().toLowerCase() + "%");
        }
        if (filter.getPhone() != null) {
            builder.append(" and p.phone = :phone ");
            params.put("phone", filter.getPhone());
        }
        if (filter.getRole() != null && !filter.getRole().equals("NON")) {
            builder.append(" and pr.role = :profileRole ");
            params.put("profileRole", filter.getRole());
        }

        // select query
        StringBuilder selectBuilder = new StringBuilder(" select p.id," +
                " p.name, " +
                " p.surname, " +
                " p.phone, " +
                " p.photo_id, " +
                " p.status, " +
                " p.created_date, " +
                " (select string_agg(role, ',')" +
                "       from profile_role where profile_id = p.id) as profileRoles," +
                " (select string_agg(name, ',') " +
                "       from subject s " +
                "       inner join profile_subject ps on ps.subject_id = s.id where ps.profile_id = p.id) as profileSubject" +
                " from profile as p "
        );
        // count query
        StringBuilder countBuilder = new StringBuilder(" select count(p) from profile as p ");

        if (filter.getRole() != null && !filter.getRole().isBlank() && !filter.getRole().equals("NON")) {
            selectBuilder.append(" inner join profile_role as pr on pr.profile_id = p.id ");
            countBuilder.append(" inner join profile_role as pr on pr.profile_id = p.id ");
        }

        StringBuilder whereBuilder = new StringBuilder();
        whereBuilder.append(" where p.visible = true  ");

        if (companyId != null) {
            whereBuilder.append(" and p.company_id = :companyId ");
        }
        if (branchId != null) {
            whereBuilder.append(" and p.company_id in (select b.id from branch as b where b.company_id = :companyId) ");
        }

        selectBuilder.append(whereBuilder).append(builder).append(" order by p.created_date desc ");
        countBuilder.append(whereBuilder).append(builder);

        // Create and set parameters for queries
        Query selectQuery = entityManager.createNativeQuery(selectBuilder.toString());
        Query countQuery = entityManager.createNativeQuery(countBuilder.toString());

        for (Map.Entry<String, Object> p : params.entrySet()) {
            selectQuery.setParameter(p.getKey(), p.getValue());
            countQuery.setParameter(p.getKey(), p.getValue());
        }

        // set pagination
        selectQuery.setFirstResult(page * size);
        selectQuery.setMaxResults(size);

        // get result
        List<Object[]> entityList = selectQuery.getResultList();
        Long totalElement = ((Number) countQuery.getSingleResult()).longValue();

        // map result to DTO
        List<ProfileDTO> dtoList = new LinkedList<>();
        for (Object[] object : entityList) {
            ProfileDTO dto = new ProfileDTO();
            dto.setId(MapperUtil.getStringValue(object[0]));
            dto.setName(MapperUtil.getStringValue(object[1]));
            dto.setSurname(MapperUtil.getStringValue(object[2]));
            dto.setPhone(MapperUtil.getStringValue(object[3]));
            if (object[4] != null) {
                dto.setPhotoUrl(attachService.getOnlyUrl((String) object[4]));
            }
            if (object[5] != null) {
                dto.setStatus(ProfileStatus.valueOf((String) object[5]));
            }
            dto.setCreatedDate(MapperUtil.getLocalDateTimeValue(object[6]));
            dto.setRoleStr(MapperUtil.getStringValue(object[7]));
            dto.setSubjectStr(MapperUtil.getStringValue(object[8]));
            dtoList.add(dto);
        }

        return new PageImpl<>(dtoList, PageRequest.of(page, size), totalElement);
    }
}
