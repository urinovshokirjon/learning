package learning.center.uz.controller;

import com.google.gson.Gson;
import learning.center.uz.dto.attendance.StudentGroupAttendanceDTO;
import learning.center.uz.entity.attendance.DailyAttendanceEntity;
import learning.center.uz.mapper.GroupMapper;
import learning.center.uz.mapper.StudentAttendanceMapper;
import learning.center.uz.service.GroupService;
import learning.center.uz.service.attendance.DailyAttendanceService;
import learning.center.uz.service.attendance.StudentAttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/attendance")
public class AttendanceController {
    private final GroupService groupService;
    private final StudentAttendanceService studentAttendanceService;
    private final DailyAttendanceService dailyAttendanceService;

    @GetMapping("/studentList/{groupId}")
    public String getAllStudent(@PathVariable String groupId, Model model) {
        GroupMapper group = groupService.getGroupById(groupId);

        List<StudentAttendanceMapper> studentAttendanceList = studentAttendanceService.getStudentAttendanceByGroupId(groupId);

        DailyAttendanceEntity lastDailyAttendance = dailyAttendanceService.getLastDailyAttendance(groupId);

        List<LocalDate> dailyAttendanceHistory = dailyAttendanceService.getDailyAttendanceDateList(groupId);

        List<LocalDate> dates = dailyAttendanceService.calculateDates(group, lastDailyAttendance, dailyAttendanceHistory);

        LocalDate currentAttendanceDate = dailyAttendanceService.calculateCurrentAttendanceDate(dates, lastDailyAttendance);
        Gson gson = dailyAttendanceService.createGsonInstance();
        int dailyAttendanceCount = dailyAttendanceHistory.size();
        List<StudentGroupAttendanceDTO> studentGroupAttendanceList = dailyAttendanceService.generateStudentGroupAttendanceList(studentAttendanceList, dailyAttendanceHistory, dailyAttendanceCount, gson);
        DailyStudentAttendancy dailyStudentAttendancy = dailyAttendanceService.prepareDailyAttendance(studentAttendanceList, currentAttendanceDate);

        model.addAttribute("daily", dailyStudentAttendancy);
        model.addAttribute("groupId", groupId);
        model.addAttribute("sgaList", studentGroupAttendanceList);
        model.addAttribute("dates", dates);
        model.addAttribute("teacherName", group.getTeacherName());
        model.addAttribute("subjectName", group.getSubjectName());
        return "dashboard/attendance/studentList";
    }

    @GetMapping("/groupList")
    public String getAllGroup(Model model) {
        List<GroupMapper> groupList = groupService.getAllGroups();
        model.addAttribute("groupList", groupList);
        return "dashboard/attendance/groupList";
    }

    @PostMapping("/save/{groupId}")
    public String saveAttendance(@PathVariable String groupId,
                                 @ModelAttribute DailyStudentAttendancy daily) {
        dailyAttendanceService.save(groupId, daily);
        return "redirect:/attendance/studentList/" + groupId;
    }
}

