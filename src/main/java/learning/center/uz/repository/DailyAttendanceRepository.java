package learning.center.uz.repository;

import learning.center.uz.entity.GroupEntity;
import learning.center.uz.entity.attendance.DailyAttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DailyAttendanceRepository extends JpaRepository<DailyAttendanceEntity, String> {

    @Query("FROM DailyAttendanceEntity  d  inner join fetch  d.studentAttendanceList " +
            " where d.groupId=?1")
    public List<DailyAttendanceEntity> getDailyAttendanceByGroupId(String grouId);


    @Query("from DailyAttendanceEntity where  groupId = ?1 order by attendanceDate desc  limit 1")
    public DailyAttendanceEntity getLastDailyAttendanceByGroupId(String grouId);


    @Query("select attendanceDate from DailyAttendanceEntity where  groupId = ?1 order by attendanceDate asc ")
    public List<LocalDate> getDailyAttendanceDateListByGroupId(String grouId);


}
