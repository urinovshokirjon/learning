package learning.center.uz.repository;

import jakarta.transaction.Transactional;
import learning.center.uz.entity.LessonPlanEntity;
import learning.center.uz.mapper.LessonPlanMapperI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LessonPlanRepository extends JpaRepository<LessonPlanEntity, String> {


    Optional<LessonPlanEntity> findByNameAndCompanyId(String lessonPlanName, String companyIdByOwnerId);

    @Query("from LessonPlanEntity as lp where lp.id = ?1 and lp.visible = true ")
    Optional<LessonPlanEntity> findByIdAndVisibleTrue(String id);

    @Transactional
    @Modifying
    @Query("update LessonPlanEntity pl set pl.deletedDate=current_timestamp, pl.visible=false, pl.deletedId=:deletedId where pl.id=:lessonPlanId")
    int deleteById(@Param("lessonPlanId") String lessonPlanId, @Param("deletedId") String deletedId);

    @Query("select  lp from LessonPlanItemEntity  lpi  inner join  lpi.lessonPlan as lp  where lpi.id =:lpiId ")
    Optional<LessonPlanEntity> findByLessonPlanItem(@Param("lpiId") String lpiId);


    List<LessonPlanMapperI> getLessonPlanBySubjectId(String subjectId);

    List<LessonPlanMapperI> findByCompanyIdAndVisibleTrue(String companyIdByOwnerId);
}
