package learning.center.uz.repository.company;

import learning.center.uz.entity.ProfileSubjectEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProfileSubjectRepository extends CrudRepository<ProfileSubjectEntity, String> {

    @Query("select p.subjectId from ProfileSubjectEntity p where p.profileId = :profileId ")
    List<String> findAllIdByProfileId(@Param("profileId") String profileId);

    @Modifying
    @Transactional
    @Query("delete from ProfileSubjectEntity p where p.profileId = :profileId and p.subjectId = :subjectId ")
    void deleteByProfileIdAndSubjectId(@Param("profileId") String profileId, @Param("subjectId") String subjectId);
}
