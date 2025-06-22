package learning.center.uz.controller;

import jakarta.validation.Valid;
import learning.center.uz.dto.branch.BranchDTO;
import learning.center.uz.dto.student.*;
import learning.center.uz.mapper.StudentGroupMapper;
import learning.center.uz.service.BranchService;
import learning.center.uz.service.ProfileService;
import learning.center.uz.service.StudentGroupService;
import learning.center.uz.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;
    private final BranchService branchService;
    private final StudentGroupService studentGroupService;

    private final ProfileService profileService;

    @GetMapping("/list")
    public String getStudentList(Model model,
                                 @RequestParam(value = "nameQuery", required = false) String nameQuery,
                                 @RequestParam(value = "phone", required = false) String phone,
                                 @RequestParam(value = "gender", required = false) String gender,
                                 @RequestParam(value = "status", required = false) String studyStatus,
                                 @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                 @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        StudentFilterDTO filterDTO = correctFilterDTO(nameQuery, phone, studyStatus, gender);
        Page<StudentResponse> result = studentService.getStudentList(filterDTO, page, size);
        model.addAttribute("studentList", result.getContent());
        model.addAttribute("totalElements", result.getTotalElements());
        model.addAttribute("totalPages", result.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("filterDTO", filterDTO);
        model.addAttribute("pageSize", 10);
        model.addAttribute("activeLink", "student");
        return "dashboard/student/list";
    }

    @GetMapping("/go/add")
    public String goToAdd(Model model) {
        List<BranchDTO> branchList = branchService.getBranchListByCompanyId();
        model.addAttribute("activeLink", "student");
        model.addAttribute("student", new StudentRequest());
        model.addAttribute("branches", branchList);
        model.addAttribute("isUpdate", false);
        return "dashboard/student/add";
    }

    @PostMapping("/add")
    public String create(@RequestParam("file") MultipartFile file,
                         @Valid @ModelAttribute StudentRequest student,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("student", student);
            model.addAttribute("isUpdate", false);
            return "dashboard/student/add";
        }
        studentService.create(student, file);
        return "redirect:/student/list";
    }

    @GetMapping("/detail/{id}")
    public String getStudentDetail(@PathVariable("id") String studentId, Model model,
                                   @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                   @RequestParam(value = "size", required = false, defaultValue = "10") int size) {

        StudentResponse student = studentService.getStudentById(studentId);
        model.addAttribute("student", student);

        Page<StudentGroupMapper> result = studentGroupService.getStudentGroupList(studentId, page, size);
        model.addAttribute("studentGroupList", result.getContent());
        model.addAttribute("totalElements", result.getTotalElements());
        model.addAttribute("totalPages", result.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", 10);
        model.addAttribute("activeLink", "student_group");
        return "dashboard/student/detail";
    }

    @PostMapping("/update-study-status")
    public String updateStudyStatus(@ModelAttribute StudentUpdateStatusDTO dto) {

        studentService.updateStudyStatus(dto.getStudentId(), dto.getStatus(), dto.getMessage());
        return "redirect:/student/detail/" + dto.getStudentId();
    }

    @GetMapping("/go/update/{id}")
    public String updateById(@PathVariable("id") String studentId, Model model) {
        StudentResponse student = studentService.getStudentById(studentId);
        List<BranchDTO> branchList = branchService.getBranchListByCompanyId();
        model.addAttribute("student", student);
        model.addAttribute("branches", branchList);
        model.addAttribute("isUpdate", true);
        return "dashboard/student/add";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") String studentId,
                         @RequestParam("file") MultipartFile file,
                         @Valid @ModelAttribute StudentRequest studentRequest,
                         BindingResult bindingResult,
                         Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("student", studentRequest);
            model.addAttribute("isUpdate", true);
            return "dashboard/student/add";
        }

        studentService.update(studentId, studentRequest, file);
        return "redirect:/student/list";
    }

    @PostMapping("/delete")
    public String delete(@Valid @ModelAttribute StudentDeleteDTO dto) {
        boolean result = studentService.delete(dto.getId());
        return "redirect:/student/list";
    }

    private StudentFilterDTO correctFilterDTO(String nameQuery, String phone, String studyStatus, String gender) {
        StudentFilterDTO filterDTO = new StudentFilterDTO();
        if (nameQuery == null || nameQuery.isBlank() || nameQuery.equals("null")) {
            filterDTO.setNameQuery(null);
        } else {
            filterDTO.setNameQuery(nameQuery);
        }
        if (studyStatus == null || studyStatus.isBlank() || studyStatus.equals("null")) {
            filterDTO.setStudyStatus("NON");
        } else {
            filterDTO.setStudyStatus(studyStatus);
        }
        if (phone == null || phone.isBlank() || phone.equals("null")) {
            filterDTO.setPhone(null);
        } else {
            filterDTO.setPhone(phone);
        }

        if (gender == null || gender.isBlank() || gender.equals("null")) {
            filterDTO.setGender("NON");
        } else {
            filterDTO.setGender(gender);
        }
        return filterDTO;
    }
}
