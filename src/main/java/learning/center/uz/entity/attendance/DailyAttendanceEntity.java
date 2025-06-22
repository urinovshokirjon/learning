package learning.center.uz.entity.attendance;

import jakarta.persistence.*;
import learning.center.uz.entity.BaseEntity;
import learning.center.uz.entity.GroupEntity;
import learning.center.uz.enums.AttendanceStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "daily_attendance")
@Getter
@Setter
public class DailyAttendanceEntity extends BaseEntity {

    @Column(name = "attendance_date")
    private LocalDate attendanceDate;

    @Column(name = "group_id")
    private String groupId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", insertable = false, updatable = false)
    private GroupEntity group;

    @Column(name = "attendance_status")
    @Enumerated(EnumType.STRING)
    private AttendanceStatus attendanceStatus;

    @OneToMany
    private List<StudentAttendanceEntity> studentAttendanceList;
}
