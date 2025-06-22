package learning.center.uz.repository.company;

import jakarta.transaction.Transactional;
import learning.center.uz.entity.company.CompanyEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CompanyRepository extends CrudRepository<CompanyEntity, String> {
    @Modifying
    @Transactional
    @Query("update CompanyEntity c set c.deletedDate = current_timestamp, c.deletedId=:deletedId, c.visible=false where c.id=:companyId")
    int deleteById(@Param("companyId") String companyId, @Param("deletedId") String deletedId);

    @Query("from CompanyEntity c where c.visible=true order by c.createdDate desc ")
    Iterable<CompanyEntity> getAll();

    @Query("from CompanyEntity c where c.id=?1 and c.visible=true ")
    Optional<CompanyEntity> getById(String id);

    @Query("select c.id from CompanyEntity c where c.ownerId =?1 and c.visible=true ")
    String getCompanyIdByOwnerId(String owner);

//    @Query("select c.id from CompanyEntity c where c.ownerId =?1 ")
//    String getCompanyIdByProfileId(String profileId);
}
