package learning.center.uz.controller;

import jakarta.validation.Valid;
import learning.center.uz.dto.lesson.LessonPlanDTO;
import learning.center.uz.dto.lesson.LessonPlanItemDTO;
import learning.center.uz.dto.lesson.LessonPlanItemDeleteDTO;
import learning.center.uz.dto.lesson.LessonPlanItemFilterDTO;
import learning.center.uz.mapper.LessonPlanItemMapperI;
import learning.center.uz.service.LessonPlanItemService;
import learning.center.uz.service.LessonPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/lesson_plan_item")
public class LessonPlanItemController {


    private final LessonPlanItemService lessonPlanItemService;
    private final LessonPlanService lessonPlanService;


    @GetMapping("/list/{lpId}")
    public String getLessonPlanItem(@PathVariable("lpId") String lessonPlanId, Model model) {
        List<LessonPlanItemMapperI> items = lessonPlanService.getItemsByLessonPlanId(lessonPlanId);
        LessonPlanDTO lpn = lessonPlanService.getById(lessonPlanId);
        model.addAttribute("items", items);
        model.addAttribute("lessonPlanName", lpn.getLessonPlanName());
        model.addAttribute("currentLessonPlanId", lessonPlanId);
        return "dashboard/lessonPlanItem/list";
    }

    @GetMapping("/go/add/{lessonPlanId}")
    public String goToAdd(@PathVariable("lessonPlanId") String lessonPlanId, Model model) {
        model.addAttribute("isUpdate", false);
        model.addAttribute("activeLink", "lessonPlanItem");
        model.addAttribute("lessonPlanItem", new LessonPlanItemDTO());
        model.addAttribute("lessonPlanId");
        return "dashboard/lessonPlanItem/add";
    }


    @PostMapping("/add/{lessonPlanId}")
    public String create(@PathVariable("lessonPlanId") String lessonPlanId, @Valid @ModelAttribute LessonPlanItemDTO lessonPlanItemDTO, Model model) {
        model.addAttribute("isUpdate", false);
        model.addAttribute("activeLink", "lessonPlanItem");
        model.addAttribute("lessonPlanItem", lessonPlanItemDTO);
        lessonPlanItemService.create(lessonPlanItemDTO, lessonPlanId);
        return "redirect:/lessonPlan/items/" + lessonPlanId;
    }


    @GetMapping("/go/update/{lpiId}")
    public String updateById(@PathVariable("lpiId") String lpiId, Model model) {
        LessonPlanItemDTO lpiDto = lessonPlanItemService.getById(lpiId);
        model.addAttribute("isUpdate", true);
        model.addAttribute("lessonPlanItem", lpiDto);
        model.addAttribute("activeLink", "lessonPlanItem");
        model.addAttribute("lessonPlanId", lessonPlanService.getLessonPlanByLessonPlanItemId(lpiId).getId());

        return "dashboard/lessonPlanItem/add";
    }


    @PostMapping("/update/{lpiId}")
    public String update(@PathVariable("lpiId") String lpiId,
                         @Valid @ModelAttribute LessonPlanItemDTO lpiDto,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("lessonPlanItem", lpiDto);
            return "dashboard/lessonPlanItem/update";
        }
        lessonPlanItemService.update(lpiId, lpiDto);
        return "redirect:/lessonPlan/items/" + lessonPlanService.getLessonPlanByLessonPlanItemId(lpiId).getId(); // go to page
    }


    @PostMapping("/delete")
    public String delete(@Valid @ModelAttribute LessonPlanItemDeleteDTO lpiDto) {
        lessonPlanItemService.delete(lpiDto.getId());
        return "redirect:/lessonPlan/list";
    }

    public LessonPlanItemFilterDTO lessonPlanItemFilter(String title, String description, String orderNumber, String homework, String lessonPlanName) {
        LessonPlanItemFilterDTO lessonPlanItemFilterDTO = new LessonPlanItemFilterDTO();
        if (title == null || title.isBlank() || title.equals("null")) {
            lessonPlanItemFilterDTO.setTitle(null);
        } else {
            lessonPlanItemFilterDTO.setTitle(title);
        }
        if (description == null || description.isBlank() || description.equals("null")) {
            lessonPlanItemFilterDTO.setDescription(null);
        } else {
            lessonPlanItemFilterDTO.setDescription(description);
        }
        if (orderNumber == null || orderNumber.isBlank() || orderNumber.equals("null")) {
            lessonPlanItemFilterDTO.setOrderNumber(null);
        } else {
            lessonPlanItemFilterDTO.setOrderNumber(orderNumber);
        }
        if (homework == null || homework.isBlank() || homework.equals("null")) {
            lessonPlanItemFilterDTO.setHomework(null);
        } else {
            lessonPlanItemFilterDTO.setHomework(homework);
        }
        if (lessonPlanName == null || lessonPlanName.isBlank() || lessonPlanName.equals("null")) {
            lessonPlanItemFilterDTO.setLessonPlanName(null);
        } else {
            lessonPlanItemFilterDTO.setLessonPlanName(lessonPlanName);
        }
        return lessonPlanItemFilterDTO;
    }


}
