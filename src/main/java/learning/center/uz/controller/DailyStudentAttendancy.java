package learning.center.uz.controller;

import learning.center.uz.dto.attendance.StudentAttendanceDTO;

import java.time.LocalDate;
import java.util.List;

public class DailyStudentAttendancy {
    private LocalDate currentAttendanceDate;
    private List<StudentAttendanceDTO> attendancyList;

    public List<StudentAttendanceDTO> getAttendancyList() {
        return attendancyList;
    }

    public void setAttendancyList(List<StudentAttendanceDTO> attendancyList) {
        this.attendancyList = attendancyList;
    }

    public LocalDate getCurrentAttendanceDate() {
        return currentAttendanceDate;
    }

    public void setCurrentAttendanceDate(LocalDate currentAttendanceDate) {
        this.currentAttendanceDate = currentAttendanceDate;
    }
}
