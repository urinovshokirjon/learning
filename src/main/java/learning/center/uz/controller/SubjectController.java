package learning.center.uz.controller;

import jakarta.validation.Valid;
import learning.center.uz.dto.subject.SubjectDTO;
import learning.center.uz.dto.subject.SubjectDeleteDTO;
import learning.center.uz.dto.subject.SubjectFilterDTO;
import learning.center.uz.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/subject")
public class SubjectController {
    private final SubjectService subjectService;

    @GetMapping("/list")
    public String getSubjectList(Model model,
                                 @RequestParam(value = "nameQuery", required = false) String nameQuery,
                                 @RequestParam(value = "name", required = false) String name,
                                 @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                 @RequestParam(value = "size", required = false, defaultValue = "10") int size
                                 ) {

        SubjectFilterDTO filterDTO = correctFilterDTO(name, nameQuery);
        Page<SubjectDTO> result = subjectService.getSubjectList(filterDTO, page, size);
        model.addAttribute("subjectList", result.getContent());
        model.addAttribute("totalElements", result.getTotalElements());
        model.addAttribute("totalPages", result.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("filterDTO", filterDTO);
        model.addAttribute("pageSiz", 10);
        model.addAttribute("activeLink", "subject");
        return "dashboard/subject/list";
    }


    @GetMapping("/go/add")
    public String goToAdd(Model model) {
        model.addAttribute("activeLink", "profile");
        model.addAttribute("subject", new SubjectDTO());
        model.addAttribute("isUpdate", false);
        return "dashboard/subject/add";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute SubjectDTO dto,
                         Model model) {
        model.addAttribute("isUpdate", false);
        subjectService.create(dto);
        return "redirect:/subject/list";
    }

    @GetMapping("/go/update/{id}")
    public String updateById(@PathVariable("id") String id,
                             @ModelAttribute SubjectDTO subjectDTO,
                             Model model) {
        SubjectDTO subject = subjectService.getBySubjectId(id);
        model.addAttribute("isUpdate", true);
        model.addAttribute("subject", subject);
        return "dashboard/subject/add";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") String id,
                         @ModelAttribute SubjectDTO dto) {
        subjectService.update(id, dto);
        return "redirect:/subject/list";
    }


    // ################
    @PostMapping("/delete")
    public String delete(@Valid @ModelAttribute SubjectDeleteDTO dto) {
        System.out.println("Delete SubjectDTO: " + dto.getId());
        subjectService.delete(dto.getId());
        return "redirect:/subject/list";
    }


    public SubjectFilterDTO correctFilterDTO(String name, String nameQuery) {
        SubjectFilterDTO dto = new SubjectFilterDTO();
        if (nameQuery == null || nameQuery.isBlank() || nameQuery.equals("null")) {
            dto.setNameQuery(null);
        } else {
            dto.setNameQuery(nameQuery);
        }
        if (name == null || name.isBlank() || name.equals("null")) {
            dto.setName(null);
        } else {
            dto.setName(name);
        }
        return dto;
    }

}
