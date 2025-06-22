package learning.center.uz.repository.custom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import learning.center.uz.dto.branch.BranchDTO;
import learning.center.uz.dto.branch.BranchFilterDTO;
import learning.center.uz.enums.AppLanguage;
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
public class BranchCustomRepository {
    private final EntityManager entityManager;
    private final AttachService attachService;

    public PageImpl<BranchDTO> filter(BranchFilterDTO filter, AppLanguage appLanguage, int page, int size, String companyId) {
        StringBuilder builder = new StringBuilder(" AND b.visible=true ");
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("companyId", companyId);

        if (filter.getBranchName() != null) {
            builder.append(" AND LOWER(b.name) LIKE :branchName ");
            params.put("branchName", "%" + filter.getBranchName().toLowerCase() + "%");
        }
        if (filter.getBranchAddress() != null) {
            builder.append(" AND LOWER(b.branch_address) LIKE :branchAddress ");
            params.put("branchAddress", "%" + filter.getBranchAddress() + "%");
        }
        if (filter.getBranchContact() != null) {
            builder.append(" AND b.contact LIKE :branchContact ");
            params.put("branchContact", "%" + filter.getBranchContact() + "%");
        }
        if (filter.getBranchManagerQuery() != null) {
            builder.append(" AND (LOWER(p.name) LIKE :branchManagerQuery OR LOWER(p.surname) LIKE :branchManagerQuery) ");
            params.put("branchManagerQuery", "%" + filter.getBranchManagerQuery() + "%");
        }

        // join table
        StringBuilder joinProfile = new StringBuilder(" INNER JOIN profile AS p ON p.id = b.manager_id ");
        StringBuilder joinCompany = new StringBuilder(" INNER JOIN company AS c ON c.id = b.company_id WHERE c.id = :companyId ");

        // select query
        StringBuilder selectBuilder = new StringBuilder(" SELECT " +
                " b.id, " +
                " p.name, " +
                " p.surname, " +
                " p.phone, " +
                " b.branch_address, " +
                " b.contact, " +
                " b.name, " +
                " b.photo_id, " +
                " b.created_date " +
                "FROM branch AS b ")
                .append(joinProfile)
                .append(joinCompany)
                .append(builder)
                .append(" ORDER BY b.created_date DESC ");

        // count query
        StringBuilder countBuilder = new StringBuilder(" SELECT " +
                " COUNT(b) FROM branch AS b ")
                .append(joinProfile)
                .append(joinCompany)
                .append(builder);

        //
        Query selectQuery = entityManager.createNativeQuery(selectBuilder.toString());
        Query countQuery = entityManager.createNativeQuery(countBuilder.toString());

        // set parameters
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
        List<BranchDTO> dtoList = new LinkedList<>();
        for (Object[] object : entityList) {
            BranchDTO dto = new BranchDTO();
            dto.setId(MapperUtil.getStringValue(object[0]));
            dto.setManagerStr(MapperUtil.getStringValue(object[1]) + "  " +
                    MapperUtil.getStringValue(object[2]) + "  " +
                    MapperUtil.getStringValue(object[3]));
            dto.setBranchAddress(MapperUtil.getStringValue(object[4]));
            dto.setContact(MapperUtil.getStringValue(object[5]));
            dto.setBranchName(MapperUtil.getStringValue(object[6]));
            if (object[7] != null) {
                dto.setPhotoUrl(attachService.getOnlyUrl((String) object[7]));
            }
            dto.setCreatedDate(MapperUtil.getLocalDateTimeValue(object[8]));

            dtoList.add(dto);
        }
        return new PageImpl<>(dtoList, PageRequest.of(page, size), totalElement);
    }
}
