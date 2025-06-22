package learning.center.uz.repository;

import jakarta.transaction.Transactional;
import learning.center.uz.entity.GroupEntity;
import learning.center.uz.mapper.GroupMapper;
import learning.center.uz.mapper.GroupNamesMapper;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends CrudRepository<GroupEntity, String> {
    @Query("  SELECT " +
            " g.id AS id, " +
            " g.name AS groupName, " +
            " (SELECT sb.name FROM SubjectEntity AS sb " +
            " WHERE g.subjectId = sb.id ) AS subjectName, " +
            " (SELECT p.name FROM ProfileEntity AS p " +
            " WHERE g.teacherId = p.id) AS teacherName " +
            " FROM GroupEntity AS g " +
            " INNER JOIN g.branch AS b " +
            " WHERE (CASE " +
            "             WHEN :isCompany = true THEN b.companyId " +
            "             ELSE g.branchId END) = :id " +
            " AND g.visible = true " +
            " AND g.id NOT IN (SELECT sg.groupId FROM StudentGroupEntity AS sg " +
            " WHERE sg.studentId = :studentId and sg.visible = true) " +
            " ORDER BY g.createdDate DESC ")
    List<GroupNamesMapper> getGroupNames(@Param("id") String id,
                                         @Param("studentId") String studentId,
                                         @Param("isCompany") Boolean isCompany);


    @Modifying
    @Transactional
    @Query("update GroupEntity c set c.deletedDate = current_timestamp, c.branchId=:deletedId, c.visible=false where c.id=:groupId")
    int deleteById(@Param("groupId") String groupId, @Param("deletedId") String deletedId);

    @Query("FROM GroupEntity c where c.visible=true order by c.createdDate desc ")
    Iterable<GroupEntity> getAll();

    @Query("FROM GroupEntity c where c.id=?1 and c.visible=true ")
    Optional<GroupEntity> getById(String id);

    @Query("SELECT" +
            " g.id AS id," +
            " g.name AS name," +
            " g.startDate AS startDate, " +
            " g.duration AS duration, " +
            " s.name AS subjectName," +
            " p.surname AS teacherSurname, " +
            " COUNT(sg.studentId) AS studentCount," +
            " p.name AS teacherName," +
            " p.phone AS teacherPhone " +
            " FROM GroupEntity AS g " +
            " INNER JOIN SubjectEntity AS s ON g.subjectId = s.id " +
            " INNER JOIN ProfileEntity AS p ON g.teacherId = p.id " +
            " INNER JOIN StudentGroupEntity AS sg ON sg.groupId = g.id " +
            " WHERE g.branchId in (select b.id from BranchEntity as b where b.companyId = :companyId) and sg.visible = true " +
            " GROUP BY  g.id, g.name, g.startDate, g.duration, s.name, p.surname, p.name, p.phone ")
    List<GroupMapper> getAllGroupsByCompanyId(@Param("companyId") String companyId);

    @Query("SELECT" +
            " g.id as id," +
            " g.name as name," +
            " g.startDate as startDate, " +
            " g.duration as duration, " +
            " s.name as subjectName," +
            " p.surname as teacherSurname, " +
            " p.name as teacherName," +
            " p.phone as teacherPhone " +
            " FROM GroupEntity AS g " +
            " INNER JOIN SubjectEntity AS s ON g.subjectId = s.id " +
            " INNER JOIN ProfileEntity AS p ON g.teacherId = p.id" +
            " where g.branchId = :branchId")
    List<GroupMapper> getAllGroupsByBranchId(String branchId);

    @Query("SELECT" +
            " g.id as id," +
            " g.name as name," +
            " g.startDate as startDate, " +
            " g.duration as duration, " +
            " s.name as subjectName," +
            " p.surname as teacherSurname, " +
            " p.name as teacherName," +
            " p.phone as teacherPhone " +
            " FROM GroupEntity AS g " +
            " INNER JOIN SubjectEntity AS s ON g.subjectId = s.id " +
            " INNER JOIN ProfileEntity AS p ON g.teacherId = p.id" +
            " where g.teacherId = :teacherId")
    List<GroupMapper> getAllGroupsByTeacherId(String teacherId);

    @Query("SELECT" +
            " g.id as id," +
            " g.name as name," +
            " g.startDate as startDate, " +
            " g.duration as duration, " +
            " s.name as subjectName," +
            " p.surname as teacherSurname, " +
            " p.name as teacherName," +
            " p.phone as teacherPhone, " +
            " (SELECT STRING_AGG(gs.dayOfWeek, ',') FROM GroupScheduleEntity gs WHERE gs.groupId = g.id) as lessonDays " +
            " FROM GroupEntity AS g " +
            " INNER JOIN SubjectEntity AS s ON g.subjectId = s.id " +
            " INNER JOIN ProfileEntity AS p ON g.teacherId = p.id " +
            " WHERE g.id = :groupId")
    GroupMapper getGroupById(@Param("groupId") String groupId);
}
