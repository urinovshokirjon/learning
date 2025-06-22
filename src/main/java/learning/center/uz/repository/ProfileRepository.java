package learning.center.uz.repository;

import jakarta.persistence.criteria.From;
import jakarta.transaction.Transactional;
import learning.center.uz.entity.ProfileEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends CrudRepository<ProfileEntity, String> {
    @Query("from ProfileEntity as p where p.visible=true and p.phone=?1")
    Optional<ProfileEntity> findByPhone(String phone);

    @Query(value = "SELECT * FROM profile WHERE phone=?1 AND password=?2 AND visible=true AND status='ACTIVE' ", nativeQuery = true)
    Optional<ProfileEntity> getProfile(String phone, String password);

    @Modifying
    @Transactional
    @Query("update ProfileEntity p set p.deletedDate = current_timestamp, p.deletedId=:deletedId, p.visible=false where p.id=:profileId")
    int deleteById(@Param("profileId") String profileId, @Param("deletedId") String deletedId);


    @Query("from ProfileEntity as p where p.visible=true order by p.createdDate desc ")
    Iterable<ProfileEntity> getProfileList();

    @Query("from ProfileEntity as p where p.id=?1 and p.visible=true ")
    Optional<ProfileEntity> getProfileById(String id);

    @Query(" select p from ProfileEntity p " +
            " inner join ProfileRoleEntity pr on pr.profileId = p.id" +
            " where p.visible = true and pr.role = 'ROLE_COMPANY' ")
    List<ProfileEntity> getRoleCompany();

    @Query(" select p from ProfileEntity p " +
            " inner join ProfileRoleEntity pr on pr.profileId = p.id" +
            " where p.visible = true and p.companyId = :companyId and pr.role = 'ROLE_COMPANY_MANAGER' ")
    List<ProfileEntity> getRoleCompanyManager(String companyId);

    @Query(" select p from ProfileEntity p " +
            " inner join ProfileRoleEntity pr on pr.profileId = p.id " +
            " where p.visible = true and p.companyId = :companyId and pr.role = 'ROLE_TEACHER' ")
    List<ProfileEntity> getTeacherList(String companyId);
}
