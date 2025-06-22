package learning.center.uz.repository;

import jakarta.transaction.Transactional;
import learning.center.uz.entity.BranchEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BranchRepository extends CrudRepository<BranchEntity, String> {


    @Query("from BranchEntity as b where b.id = ?1 and b.visible = true ")
    Optional<BranchEntity> findByIdAndVisibleTrue(String branchId);

    Optional<BranchEntity> findByNameAndCompanyId(String branchName, String companyId);

    @Modifying
    @Transactional
    @Query("update BranchEntity b set b.deletedDate = current_timestamp, b.deletedId=:deletedId, b.visible=false where b.id=:branchId")
    int deleteById(@Param("branchId") String branchId, @Param("deletedId") String deletedId);

    @Query("select b.id from BranchEntity b where b.managerId = :managerId ")
    String getBranchIdByManagerId(@Param("managerId") String managerId);

    @Query("select b from BranchEntity b where b.companyId = :companyId and b.visible = true")
    List<BranchEntity> findAllByCompanyId(@Param("companyId") String companyId);
}
