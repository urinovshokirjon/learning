package learning.center.uz.dto.attendance;

import jakarta.persistence.Column;
import learning.center.uz.enums.AttendanceStatus;
import learning.center.uz.enums.HomeworkStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class StudentAttendanceDTO {

    ///
    private LocalDate attendanceDate;
    private Boolean came;
    private HomeworkStatus homeworkStatus;

    ///////////////////////////////////
    private AttendanceStatus attendanceStatus;
    private String id;
    private String name;
    private String surname;
    private String phone;

    public StudentAttendanceDTO() {
    }

    public StudentAttendanceDTO(String id) {
        this.id = id;
    }

    public StudentAttendanceDTO(String id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

}
