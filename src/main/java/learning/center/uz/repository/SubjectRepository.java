package learning.center.uz.repository;

import jakarta.transaction.Transactional;
import learning.center.uz.entity.SubjectEntity;
import learning.center.uz.mapper.SubjectMapperI;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends CrudRepository<SubjectEntity, String> {

    @Query(value = " SELECT s.id AS id, s.name AS name FROM SubjectEntity AS s WHERE s.companyId = ?1 AND s.visible=true ")
    List<SubjectMapperI> findByCompanyId(String companyId);


    @Transactional
    @Modifying
    @Query("update SubjectEntity as s set s.deletedDate = current_timestamp,  s.visible= false where s.id=:subjectId")
    Integer deleteSubject(@Param("subjectId") String subjectId);

    @Query("from SubjectEntity as s where s.visible=true and s.id=?1")
    Optional<SubjectEntity> getSubjectById(String subjectId);

    @Query("from SubjectEntity as s where s.visible=true and s.name=?1")
    Optional<SubjectEntity> getByName(String name);

    @Query(" from SubjectEntity as s " +
            " inner join ProfileSubjectEntity ps on s.id = ps.subjectId " +
            " where s.visible=true and ps.profileId=?1")
    List<SubjectEntity> getByProfileId(String profileId);
}
