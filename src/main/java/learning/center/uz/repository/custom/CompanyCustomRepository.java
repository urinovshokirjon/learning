package learning.center.uz.repository.custom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import learning.center.uz.dto.admin.company.CompanyDTO;
import learning.center.uz.dto.admin.company.CompanyFilterDTO;
import learning.center.uz.dto.admin.profile.ProfileDTO;
import learning.center.uz.enums.AppLanguage;
import learning.center.uz.service.AttachService;
import learning.center.uz.service.ProfileService;
import learning.center.uz.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Repository
public class CompanyCustomRepository {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private AttachService attachService;
    @Autowired
    private ProfileService profileService;

    public PageImpl<CompanyDTO> filter(CompanyFilterDTO filter, AppLanguage appLanguage, int page, int size) {

        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new LinkedHashMap<>();

        if (filter.getOwner() != null) {
            builder.append(" inner join profile on profile.id=c.owner where c.visible = true and lower(profile.name) like :owner ");
            params.put("owner", filter.getOwner().toLowerCase());
        } else {
            builder.append(" where c.visible=true ");
        }
        if (filter.getName() != null) {
            builder.append(" and (lower(c.name) like :name) ");
            params.put("name", "%" + filter.getName().toLowerCase() + "%");
        }
        if (filter.getContact() != null) {
            builder.append(" and c.contact = :contact ");
            params.put("contact", filter.getContact());
        }
        // select query
        StringBuilder selectBuilder = new StringBuilder("select c.id, c.name, c.contact, c.owner, c.photo, c.created_date ");
        selectBuilder.append(" from company as c ");
        // count query
        StringBuilder countBuilder = new StringBuilder("select count(c) from company as c ");

        selectBuilder.append(builder);
        selectBuilder.append(" order by c.created_date desc ");
        countBuilder.append(builder);

        //
        Query selectQuery = entityManager.createNativeQuery(selectBuilder.toString());
        Query countQuery = entityManager.createNativeQuery(countBuilder.toString());
        // set parameters
        // selectQuery.setParameter("lang", appLanguage.name());
        for (Map.Entry<String, Object> p : params.entrySet()) {
            selectQuery.setParameter(p.getKey(), p.getValue());
            countQuery.setParameter(p.getKey(), p.getValue());
        }
        // set pagination
        selectQuery.setFirstResult(page * size);
        selectQuery.setMaxResults(size);
        // get result
        List<Object[]> entityList = selectQuery.getResultList();
        Long totalElement = (Long) countQuery.getSingleResult();
        //
        List<CompanyDTO> dtoList = new LinkedList<>();
        for (Object[] object : entityList) {
            CompanyDTO dto = new CompanyDTO();
            dto.setId(MapperUtil.getStringValue(object[0]));
            dto.setName(MapperUtil.getStringValue(object[1]));
            dto.setContact(MapperUtil.getStringValue(object[2]));
            ProfileDTO profile = profileService.getProfileById(MapperUtil.getStringValue(object[3]));
            dto.setOwner(profile);
            if (object[4] != null) {
                dto.setPhotoUrl(attachService.getOnlyUrl((String) object[4]));
            }
            dto.setCreatedDate(MapperUtil.getLocalDateTimeValue(object[5]));
            dtoList.add(dto);
        }
        return new PageImpl<>(dtoList, PageRequest.of(page, size), totalElement);
    }

}
