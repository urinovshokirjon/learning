package learning.center.uz.repository;

import jakarta.transaction.Transactional;
import learning.center.uz.entity.StudentEntity;
import learning.center.uz.enums.StudyStatus;
import learning.center.uz.mapper.StudentMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, String>,
        PagingAndSortingRepository<StudentEntity, String> {

    @Query("select s from StudentEntity s where s.phone = ?1")
    Optional<StudentEntity> findByPhone(String phone);

    @Modifying
    @Transactional
    @Query("update StudentEntity s set s.deletedDate = current_timestamp, s.deletedId=:deletedId, s.visible=false where s.id=:studentId")
    int deleteById(@Param("studentId") String studentId, @Param("deletedId") String deletedId);

    @Query(" select s " +
            " from StudentEntity s " +
            " left join StudentGroupEntity sg on sg.studentId = s.id " +
            " where sg.groupId = :groupId ")
    List<StudentEntity> getAllStudentByGroupId(@Param("groupId") String groupId);

    @Query(" select" +
            " s.id as id," +
            " s.photoId as photoId," +
            " s.name as name," +
            " s.surname as surname," +
            " s.phone as phone," +
            " sg.joinedDate as joinedDate," +
            " sg.leftDate as leftDate " +
            " from StudentEntity s " +
            " left join StudentGroupEntity sg on sg.studentId = s.id " +
            " where sg.groupId = :groupId and sg.visible = true ")
    List<StudentMapper> findAllStudentByGroupId(@Param("groupId") String groupId);

    @Query(" select s " +
            " from StudentEntity s " +
            " where s.branchId = (select g.branchId from GroupEntity g where g.id = :groupId) " +
            " and s.id not in (select sg.studentId from StudentGroupEntity sg where sg.groupId = :groupId and sg.visible = true)")
    List<StudentEntity> getBranchStudentsByGroupId(String groupId);

    @Modifying
    @Transactional
    @Query(" UPDATE StudentEntity s SET s.studyStatus = :status, " +
            " s.message = :message, " +
            " s.updatedDate = CURRENT_TIMESTAMP " +
            " WHERE s.id = :studentId ")
    void updateStudyStatus(@Param("studentId") String studentId,
                           @Param("status") StudyStatus status,
                           @Param("message") String message
    );
}
