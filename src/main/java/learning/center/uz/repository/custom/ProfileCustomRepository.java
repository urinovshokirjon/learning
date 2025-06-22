//package learning.center.uz.repository.custom;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.Query;
//import learning.center.uz.dto.admin.profile.ProfileDTO;
//import learning.center.uz.dto.admin.profile.ProfileFilterDTO;
//import learning.center.uz.enums.AppLanguage;
//import learning.center.uz.enums.ProfileStatus;
//import learning.center.uz.service.AttachService;
//import learning.center.uz.util.MapperUtil;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.stereotype.Repository;
//
//import java.util.LinkedHashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//
//@Repository
//@RequiredArgsConstructor
//public class ProfileCustomRepository {
//    private final EntityManager entityManager;
//    private final AttachService attachService;
//
//    public PageImpl<ProfileDTO> filter(ProfileFilterDTO filter, AppLanguage appLanguage, int page, int size, String companyId) {
//        StringBuilder builder = new StringBuilder(" and p.visible=true ");
//        Map<String, Object> params = new LinkedHashMap<>();
//
//        if (filter.getNameQuery() != null) {
//            builder.append(" and (lower(p.name) like :queryParam or lower(p.surname) like :queryParam) ");
//            params.put("queryParam", "%" + filter.getNameQuery().toLowerCase() + "%");
//        }
//        if (filter.getPhone() != null) {
//            builder.append(" and p.phone = :phone ");
//            params.put("phone", filter.getPhone());
//        }
//        if (filter.getRole() != null && !filter.getRole().equals("NON")) {
//            builder.append(" and pr.role = :profileRole ");
//            params.put("profileRole", filter.getRole());
//        }
//        // select query
//        StringBuilder selectBuilder = new StringBuilder("select p.id, p.name, p.surname, p.phone, p.photo_id, p.status, p.created_date," +
//                " (select string_agg(role,',') from profile_role where profile_id = p.id) as profileRoles  ");
//        if (companyId != null && !companyId.isEmpty()){
//            selectBuilder.append(" (select string_agg(subject,',') from subject s inner join profile_subject ps where ps.profile_id = p.id) as profileSubject ");
//        }
//        selectBuilder.append(" from profile as p ");
//
//        // count query
//        StringBuilder countBuilder = new StringBuilder("select count(p) from profile as p ");
//
//        if (filter.getRole() != null && !filter.getRole().isBlank() & !filter.getRole().equals("NON")) {
//            selectBuilder.append(" inner join profile_role as pr on pr.profile_id = p.id ");
//            countBuilder.append(" inner join profile_role as pr on pr.profile_id = p.id ");
//        }
//
//        if (companyId != null && !companyId.isEmpty()) {
//            String companyProfile = " inner join company_profile cp on p.id = cp.profile_id where cp.company_id = :companyId ";
//            selectBuilder.append(companyProfile);
//            countBuilder.append(companyProfile);
//        } else {
//            selectBuilder.append(" where 1=1 ");
//            countBuilder.append(" where 1=1 ");
//        }
//
//        selectBuilder.append(builder).append(" order by p.created_date desc ");
//        countBuilder.append(builder);
//
//        //
//        Query selectQuery = entityManager.createNativeQuery(selectBuilder.toString());
//        Query countQuery = entityManager.createNativeQuery(countBuilder.toString());
//        // set parameters
//        // selectQuery.setParameter("lang", appLanguage.name());
//        for (Map.Entry<String, Object> p : params.entrySet()) {
//            selectQuery.setParameter(p.getKey(), p.getValue());
//            countQuery.setParameter(p.getKey(), p.getValue());
//        }
//
//        if (companyId != null && !companyId.isEmpty()) {
//            selectQuery.setParameter("companyId", companyId);
//            countQuery.setParameter("companyId", companyId);
//
//        }
//
//        // set pagination
//        selectQuery.setFirstResult(page * size);
//        selectQuery.setMaxResults(size);
//        // get result
//        List<Object[]> entityList = selectQuery.getResultList();
//        Long totalElement = (Long) countQuery.getSingleResult();
//        //
//        List<ProfileDTO> dtoList = new LinkedList<>();
//        for (Object[] object : entityList) {
//            ProfileDTO dto = new ProfileDTO();
//            dto.setId(MapperUtil.getStringValue(object[0]));
//            dto.setName(MapperUtil.getStringValue(object[1]));
//            dto.setSurname(MapperUtil.getStringValue(object[2]));
//            dto.setPhone(MapperUtil.getStringValue(object[3]));
//            if (object[4] != null) {
//                dto.setPhotoUrl(attachService.getOnlyUrl((String) object[4]));
//            }
//            if (object[5] != null) {
//                dto.setStatus(ProfileStatus.valueOf((String) object[5]));
//            }
//            dto.setCreatedDate(MapperUtil.getLocalDateTimeValue(object[6]));
//            dto.setRoleStr(MapperUtil.getStringValue(object[7]));
//            if (object[8] != null) {
//                dto.setSubjectStr(MapperUtil.getStringValue(object[8]));
//            }
//            dtoList.add(dto);
//        }
//        return new PageImpl<>(dtoList, PageRequest.of(page, size), totalElement);
//    }
//}

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
public class ProfileCustomRepository {
    private final EntityManager entityManager;
    private final AttachService attachService;

    public PageImpl<ProfileDTO> filter(ProfileFilterDTO filter, AppLanguage appLanguage, int page, int size) {
        StringBuilder builder = new StringBuilder(" where p.visible = true ");
        Map<String, Object> params = new LinkedHashMap<>();

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
        StringBuilder selectBuilder = new StringBuilder("" +
                " select p.id," +
                " p.name," +
                " p.surname," +
                " p.phone," +
                " p.photo_id," +
                " p.status," +
                " p.created_date, " +
                " (select string_agg(role, ',') " +
                "       from profile_role where profile_id = p.id) as profileRoles" +
                " from profile as p ");

        // count query
        StringBuilder countBuilder = new StringBuilder("select count(p) from profile as p");

        if (filter.getRole() != null && !filter.getRole().isBlank() && !filter.getRole().equals("NON")) {
            selectBuilder.append(" inner join profile_role as pr on pr.profile_id = p.id");
            countBuilder.append(" inner join profile_role as pr on pr.profile_id = p.id");
        }

        selectBuilder.append(builder).append(" order by p.created_date desc");
        countBuilder.append(builder);

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
            dtoList.add(dto);
        }

        return new PageImpl<>(dtoList, PageRequest.of(page, size), totalElement);
    }
}
