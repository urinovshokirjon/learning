package learning.center.uz.repository;

import learning.center.uz.entity.ProfileRoleEntity;
import learning.center.uz.enums.ProfileRole;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProfileRoleRepository extends CrudRepository<ProfileRoleEntity, String> {
    @Query("select role from ProfileRoleEntity  where profileId=?1")
    List<ProfileRole> roleListByProfile(String profileId);

    List<ProfileRoleEntity> findByRole(ProfileRole role);

    @Transactional
    @Modifying
    @Query("delete from ProfileRoleEntity where profileId=?1 and role =?2")
    void deleteRoleFromProfile(String profileId, ProfileRole profileRole);


}
