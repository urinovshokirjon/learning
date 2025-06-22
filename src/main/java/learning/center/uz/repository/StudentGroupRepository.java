package learning.center.uz.repository;

import jakarta.transaction.Transactional;
import learning.center.uz.entity.StudentGroupEntity;
import learning.center.uz.mapper.StudentGroupMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StudentGroupRepository extends JpaRepository<StudentGroupEntity, Long> {

    @Query(" SELECT " +
            " g.id AS groupId," +
            " g.name AS groupName," +
            " (SELECT sb.name FROM SubjectEntity AS sb" +
            " WHERE g.subjectId = sb.id ) AS subjectName," +
            " (SELECT p.name FROM ProfileEntity AS p " +
            " WHERE g.teacherId = p.id) AS teacherName," +
            " g.startDate AS groupStartDate," +
            " g.finishedDate AS groupFinishedDate," +
            " sg.joinedDate AS joinedDate," +
            " sg.leftDate AS leftDate " +
            " FROM StudentGroupEntity AS sg " +
            " INNER JOIN sg.group AS g " +
            " INNER JOIN sg.student AS s " +
            " INNER JOIN g.branch AS b " +
            " WHERE (CASE " +
            "              WHEN :isCompany = true THEN b.companyId " +
            "              ELSE g.branchId END) = :id " +
            " AND sg.studentId = :studentId " +
            " AND sg.visible = true " +
            " ORDER BY sg.createdDate DESC ")
    Page<StudentGroupMapper> getStudentGroupMapperByStudentId(Pageable pageable,
                                                              @Param("id") String id,@Param("studentId") String studentId,
                                                              @Param("isCompany") Boolean isCompany);
    @Modifying
    @Transactional
    @Query(" UPDATE StudentGroupEntity sg SET sg.visible = false, " +
            " sg.updatedDate = CURRENT_TIMESTAMP, " +
            " sg.leftDate = CURRENT_DATE " +
            " WHERE sg.id = :studentGroupId ")
    void deleteStudentFromGroup(@Param("studentGroupId") String studentGroupId);

    @Modifying
    @Transactional
    @Query(" UPDATE StudentGroupEntity  sg SET sg.visible = false, sg.leftDate = CURRENT_DATE  WHERE sg.studentId = ?1 and sg.groupId = ?2 ")
    void updateStatus(String studentId, String groupId);

    @Query("select s from StudentGroupEntity s where s.studentId = ?1 and s.groupId = ?2 and s.visible = true ")
    Optional<StudentGroupEntity> getStudentGroupByStudentIdAndGroupId(String studentId, String groupId);
}


