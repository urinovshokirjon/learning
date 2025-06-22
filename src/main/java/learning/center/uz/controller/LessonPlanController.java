package learning.center.uz.controller;

import jakarta.validation.Valid;
import learning.center.uz.dto.lesson.LessonPlanDTO;
import learning.center.uz.dto.lesson.LessonPlanDeleteDTO;
import learning.center.uz.dto.lesson.LessonPlanFilterDTO;
import learning.center.uz.mapper.LessonPlanItemMapperI;
import learning.center.uz.service.LessonPlanService;
import learning.center.uz.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/lessonPlan")
public class LessonPlanController {

    private final LessonPlanService lessonPlanService;
    private final SubjectService subjectService;


    @GetMapping("/list")
    public String getLessonPlanList(Model model,
                                    @RequestParam(value = "lessonPlanName", required = false) String lessonPlanName,
                                    @RequestParam(value = "subjectName", required = false) String subjectName,
                                    @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                    @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        LessonPlanFilterDTO filterDTO = lessonPlanFilter(lessonPlanName, subjectName);
        Page<LessonPlanDTO> result = lessonPlanService.getLessonPlanList(filterDTO, page, size);
        model.addAttribute("lessonPlanList", result.getContent());
        model.addAttribute("totalElements", result.getTotalElements());
        model.addAttribute("totalPages", result.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("filterDTO", filterDTO);
        model.addAttribute("pageSize", 10);
        model.addAttribute("activeLink", "lessonPlan");
        return "dashboard/lessonPlan/list";
    }


    @GetMapping("/go/add")
    public String goToAdd(Model model) {
        model.addAttribute("activeLink", "lessonPlan");
        model.addAttribute("lessonPlan", new LessonPlanDTO());
        model.addAttribute("subjects", subjectService.getSubjectList());
        model.addAttribute("isUpdate", false);
        return "dashboard/lessonPlan/add";

    }


    @PostMapping("/add")
    public String create(@Valid @ModelAttribute LessonPlanDTO lessonPlanDTO,
                         BindingResult bindingResult,
                         Model model) {
       /* if (bindingResult.hasErrors()) {
            model.addAttribute("lessonPlan", lessonPlanDTO);
            model.addAttribute("isUpdate", false);
            model.addAttribute("subjects", subjectService.getSubjectList());
            return "dashboard/lessonPlan/add";
        }*/
        model.addAttribute("isUpdate", false);
        lessonPlanService.create(lessonPlanDTO);
        return "redirect:/lessonPlan/list"; // go to url
    }


    @GetMapping("/go/update/{id}")
    public String updateById(@PathVariable("id") String id, Model model) {
        LessonPlanDTO lessonPlanDTO = lessonPlanService.getById(id);
        model.addAttribute("lessonPlan", lessonPlanDTO);
        model.addAttribute("subjects", subjectService.getSubjectList());
        model.addAttribute("isUpdate", true);
        return "dashboard/lessonPlan/add"; // go to page
    }


    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") String id,
                         @Valid @ModelAttribute LessonPlanDTO lessonPlanDTO,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("lessonPlan", lessonPlanDTO);
            model.addAttribute("isUpdate", true);
            return "dashboard/branch/add";
        }
        lessonPlanService.update(id, lessonPlanDTO);
        return "redirect:/lessonPlan/list"; // go to url
    }


    @PostMapping("/delete")
    public String delete(@Valid @ModelAttribute LessonPlanDeleteDTO dto) {
        lessonPlanService.delete(dto.getId());
        return "redirect:/lessonPlan/list";
    }


    public LessonPlanFilterDTO lessonPlanFilter(String lessonPlanName, String subjectName) {
        LessonPlanFilterDTO lessonPlanFilterDTO = new LessonPlanFilterDTO();
        if (lessonPlanName == null || lessonPlanName.isBlank() || lessonPlanName.equals("null")) {
            lessonPlanFilterDTO.setLessonPlanName(null);
        } else {
            lessonPlanFilterDTO.setLessonPlanName(lessonPlanName);
        }
        if (subjectName == null || subjectName.isBlank() || subjectName.equals("null")) {
            lessonPlanFilterDTO.setSubjectName(null);
        } else {
            lessonPlanFilterDTO.setSubjectName(subjectName);
        }
        return lessonPlanFilterDTO;
    }

    @GetMapping("/lessonPlanItem/go/add/{lessonPlanId}")
    public String goAddLessonPlanItem(@PathVariable("lessonPlanId") String lessonPlanId, Model model) {
        model.addAttribute("lessonPlanId", lessonPlanId);
        return "dashboard/lessonPlanItem/add";
    }

}
