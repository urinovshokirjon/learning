package learning.center.uz.controller;

import jakarta.validation.Valid;
import learning.center.uz.dto.group.GroupCreatedDTO;
import learning.center.uz.dto.group.GroupDTO;
import learning.center.uz.dto.admin.profile.ProfileDeleteDTO;
import learning.center.uz.dto.group.GroupFilterDTO;
import learning.center.uz.dto.group.StudentGroupDTO;
import learning.center.uz.service.*;
import learning.center.uz.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;
    private final SubjectService subjectService;
    private final ProfileService profileService;
    private final BranchService branchService;
    private final StudentService studentService;
    private final StudentGroupService studentGroupService;

    @GetMapping("/list")
    public String showCompanyPage(Model model,
                                  @RequestParam(value = "name", required = false) String name,
                                  @RequestParam(value = "subject", required = false) String subject,
                                  @RequestParam(value = "teacher", required = false) String teacher,
                                  @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                  @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        GroupFilterDTO filterDTO = correctFilterDTO(name, subject,teacher);
        Page<GroupDTO> result = groupService.getGroupList(filterDTO, page, size);
        model.addAttribute("groupList", result.getContent());
        model.addAttribute("totalElements", result.getTotalElements());
        model.addAttribute("totalPages", result.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("filterDTO", filterDTO);
        model.addAttribute("pageSize", 10);
        model.addAttribute("activeLink", "group");
        return "dashboard/group/list";
    }

    @GetMapping("/go/add")
    public String goToAdd(Model model) {
        model.addAttribute("activeLink", "group");
        model.addAttribute("group", new GroupDTO(false,false,false,false,false,false,false));
        model.addAttribute("subjects", subjectService.getSubjectList());
        model.addAttribute("branches", branchService.getBranchListByCompanyId());
        model.addAttribute("Teachers", profileService.getTeacherList());
        model.addAttribute("isUpdate", false);
        return "dashboard/group/add";
    }

    @PostMapping("/add")
        public String create(@ModelAttribute("group") GroupDTO dto) {
        groupService.create(dto);
        return "redirect:/group/list" ;
    }

    @GetMapping("/go/update/{id}")
    public String showUpdateForm(@PathVariable("id") String id, Model model) {
        GroupDTO groupDTO = groupService.getById(id);
        model.addAttribute("branches", branchService.getBranchListByCompanyId());
        model.addAttribute("subjects", subjectService.getSubjectList());
        model.addAttribute("group", groupDTO);
        model.addAttribute("Teachers", profileService.getTeacherList());
        model.addAttribute("isUpdate", true);
        return "dashboard/group/add";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") String groupId,
                             @Valid GroupDTO groupDTO,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            groupDTO.setId(groupId);
            model.addAttribute("subjects", subjectService.getSubjectList());
            model.addAttribute("branches", branchService.getBranchListByCompanyId());
            model.addAttribute("group", groupDTO);
            model.addAttribute("isUpdate", true);
            model.addAttribute("Teachers", profileService.getTeacherList());
            return "dashboard/group/add";
        }
        groupService.update(groupId, groupDTO);
        return "redirect:/group/list";
    }

    @PostMapping("/delete")
    public String delete(@Valid @ModelAttribute ProfileDeleteDTO dto) {
        groupService.delete(dto.getId());
        return "redirect:/group/list";
    }

    @GetMapping("/go/info/{groupId}")
    public String showInfoPage(@PathVariable("groupId") String groupId, Model model) {
        model.addAttribute("group", groupService.getById(groupId));
        model.addAttribute("studentList", studentService.findStudentByGroupId(groupId));
        return "dashboard/group/groupDetail";
    }

    @GetMapping("/go/student-group/add/{groupId}")
    public String goStudentGroupAdd(@PathVariable String groupId,
                                    Model model) {
        model.addAttribute("activeLink", "group");
        model.addAttribute("studentGroup", new StudentGroupDTO());
        model.addAttribute("groupId", groupId);
        model.addAttribute("isUpdate", false);
        model.addAttribute("studentList", studentService.getBranchStudentsByGroupId(groupId));

        return "dashboard/group/studentGroup";
    }

    @PostMapping("/student-group/add/{groupId}")
    public String create(@PathVariable String groupId,
            @ModelAttribute("studentGroup") StudentGroupDTO dto) {
        studentGroupService.save(dto);
        return "redirect:/group/go/info/"+groupId;
    }

    @PostMapping("/student-group/delete/{groupId}")
    public String deleteStudentGroup(@PathVariable String groupId,
                                     @ModelAttribute("studentGroup") StudentGroupDTO dto) {
        studentGroupService.delete(dto);
        return "redirect:/group/go/info/"+groupId;
    }


    private GroupFilterDTO correctFilterDTO(String name, String subject, String teacher) {
        GroupFilterDTO filterDTO = new GroupFilterDTO();
        if (name == null || name.isBlank() || name.equals("null")) {
            filterDTO.setName(null);
        } else {
            filterDTO.setName(name);
        }

        if (subject == null || subject.isBlank() || subject.equals("null")) {
            filterDTO.setSubject(null);
        } else {
            filterDTO.setSubject(subject);
        }

        if (teacher == null || teacher.isBlank() || teacher.equals("null")) {
            filterDTO.setTeacher(null);
        } else {
            filterDTO.setTeacher(teacher);
        }
        return filterDTO;
    }
}
