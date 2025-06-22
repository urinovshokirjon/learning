package learning.center.uz.repository;

import learning.center.uz.entity.attendance.StudentAttendanceEntity;
import learning.center.uz.mapper.StudentAttendanceMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentAttendanceRepository extends JpaRepository<StudentAttendanceEntity, String> {

    @Query(value = "select s.id as id, " +
            "       s.name as name,  " +
            "       s.surname as surname, " +
            "       get_student_attendance(:groupId,s.id) as attendanceJson " +
            " from student_group sg " +
            "         inner join student s on s.id = sg.student_id " +
            " where sg.group_id =:groupId and sg.visible=true ", nativeQuery = true)
    public List<StudentAttendanceMapper> getStudentAttendanceByGroupId(@Param("groupId") String groupId);

}
