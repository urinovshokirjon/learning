package learning.center.uz.entity.attendance;

import jakarta.persistence.*;
import learning.center.uz.entity.BaseEntity;
import learning.center.uz.entity.StudentEntity;
import learning.center.uz.enums.HomeworkStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "student_attendance")
public class StudentAttendanceEntity extends BaseEntity {

    @Column(name = "daily_attendance_id")
    private String dailyAttendanceId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_attendance_id",insertable=false,updatable=false)
    private DailyAttendanceEntity dailyAttendance;

    @Column(name = "student_id")
    private String studentId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id",insertable=false,updatable=false)
    private StudentEntity student;

    @Column(name = "came")
    private Boolean came;

    @Enumerated(EnumType.STRING)
    @Column(name = "homework_status")
    private HomeworkStatus homeworkStatus;

}
