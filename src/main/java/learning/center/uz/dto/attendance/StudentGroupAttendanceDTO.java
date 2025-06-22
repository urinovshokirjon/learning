package learning.center.uz.dto.attendance;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class StudentGroupAttendanceDTO {
    private String id;
    private String name;
    private String surname;
    private String phone;

    private List<StudentAttendanceDTO> attendanceList;
}
