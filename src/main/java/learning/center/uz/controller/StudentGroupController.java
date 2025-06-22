package learning.center.uz.controller;

import jakarta.validation.Valid;
import learning.center.uz.dto.StudentGroupRequest;
import learning.center.uz.dto.group.GroupDTO;
import learning.center.uz.dto.group.StudentGroupDTO;
import learning.center.uz.dto.student.StudentResponse;
import learning.center.uz.dto.student.StudentUpdateStatusDTO;
import learning.center.uz.service.GroupService;
import learning.center.uz.service.StudentGroupService;
import learning.center.uz.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/student_group")
@RequiredArgsConstructor
public class StudentGroupController {
    private final StudentGroupService studentGroupService;
    private final GroupService groupService;
    private final StudentService studentService;

    @GetMapping("/go/add/{id}")
    public String goToAdd(@PathVariable("id") String studentId, Model model) {
        model.addAttribute("activeLink", "student");
        model.addAttribute("studentId", studentId);
        model.addAttribute("student", new StudentGroupRequest());
        model.addAttribute("groups", groupService.getGroupNames(studentId));
        model.addAttribute("isUpdate", false);
        return "dashboard/student/detail_add";
    }

    @PostMapping("/add/{id}")
    public String create(@PathVariable("id") String studentId,
                         @Valid @ModelAttribute StudentGroupRequest studentGroupRequest,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("student", studentGroupRequest);
            model.addAttribute("studentId", studentId);
            model.addAttribute("groups", groupService.getGroupNames(studentId));
            model.addAttribute("isUpdate", false);
            return "dashboard/student/detail_add";
        }
        studentGroupService.create(studentId, studentGroupRequest);
        return "redirect:/student/detail/" + studentId;
    }

    @GetMapping("/go/update/{studentId}/{groupId}")
    public String goToUpdate(@PathVariable("studentId") String studentId,
                             @PathVariable("groupId") String groupId,
                             Model model) {
        StudentGroupDTO studentGroup = studentGroupService.getStudentGroupByStudentId(studentId, groupId);
        StudentResponse student = studentService.getStudentById(studentId);
        GroupDTO group = groupService.getById(groupId);
        model.addAttribute("studentGroup", studentGroup);
        model.addAttribute("student", student);
        model.addAttribute("group", group);
        model.addAttribute("groups", groupService.getGroupNames(studentId));
        model.addAttribute("isUpdate", true);
        return "dashboard/student/detail_student_group";
    }

    @PostMapping("/delete")
    public String deleteStudentFromGroup(@ModelAttribute StudentUpdateStatusDTO dto) {
        studentGroupService.deleteStudentFromGroup(dto.getId());
        return "redirect:/student/detail/" + dto.getStudentId();
    }

}
