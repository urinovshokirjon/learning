package learning.center.uz.repository;

import jakarta.transaction.Transactional;
import learning.center.uz.entity.LessonPlanItemEntity;
import learning.center.uz.mapper.LessonPlanItemMapperI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LessonPlanItemRepository extends JpaRepository<LessonPlanItemEntity, String> {

    Optional<LessonPlanItemEntity> findByOrderNumberOrTitle(String orderNumber, String title);

    Optional<LessonPlanItemEntity> findByIdAndVisibleTrue(String lessonPlanItemId);

    Optional<LessonPlanItemEntity> findByLessonPlanIdAndVisibleTrue(String lpId);

    @Transactional
    @Modifying
    @Query(" update LessonPlanItemEntity lpi set lpi.visible = false, lpi.deletedDate = current_timestamp, lpi.deletedId=:deletedId where lpi.id=:lpiId")
    int deleteById(@Param("deletedId") String deletedId, @Param("lpiId") String lpiId);

    @Query(value = "SELECT " +
            " lpi.id AS id, " +
            " lpi.title AS title, " +
            " lpi.description AS description, " +
            " lpi.orderNumber AS orderNumber, " +
            " lpi.homework AS homework, " +
            " lpi.createdDate AS createdDate " +
            " FROM LessonPlanItemEntity AS lpi " +
            " WHERE lpi.lessonPlanId = ?1 AND lpi.visible = true ")
    List<LessonPlanItemMapperI> getLessonPlanItemListByLessonPlanId(String lessonPlanId);

}
