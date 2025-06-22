package learning.center.uz.controller.profile;

import jakarta.validation.Valid;
import learning.center.uz.dto.admin.profile.ProfileDTO;
import learning.center.uz.dto.admin.profile.ProfileDeleteDTO;
import learning.center.uz.dto.admin.profile.ProfileFilterDTO;
import learning.center.uz.dto.admin.profile.ProfileRequest;
import learning.center.uz.service.ProfileService;
import learning.center.uz.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/company-profile")
@RequiredArgsConstructor
public class CompanyProfileController {
    private final ProfileService profileService;
    private final SubjectService subjectService;

    @GetMapping("/list")
    public String getProfileList(Model model,
                                 @RequestParam(value = "nameQuery", required = false) String nameQuery,
                                 @RequestParam(value = "phone", required = false) String phone,
                                 @RequestParam(value = "role", required = false) String role,
                                 @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                 @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        ProfileFilterDTO filterDTO = correctFilterDTO(nameQuery, phone, role);
        Page<ProfileDTO> result = profileService.getProfileList(filterDTO, page - 1, size);
        model.addAttribute("profileList", result.getContent());
        model.addAttribute("totalElements", result.getTotalElements());
        model.addAttribute("totalPages", result.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("filterDTO", filterDTO);
        model.addAttribute("pageSiz", 10);
        model.addAttribute("activeLink", "profile");
        return "dashboard/profile/company/list";
    }

    @GetMapping("/go/add")
    public String goToAdd(Model model) {
        model.addAttribute("activeLink", "profile");
        model.addAttribute("profile", new ProfileDTO());
        model.addAttribute("subjects", subjectService.getSubjectList());
        model.addAttribute("isUpdate", false);
        return "dashboard/profile/company/add";
    }

    @PostMapping("/add")
    public String create(@RequestParam("file") MultipartFile file,
                         @Valid @ModelAttribute ProfileRequest profileRequest,
                         BindingResult bindingResult,
                         Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("profile", profileRequest);
            model.addAttribute("subjects", subjectService.getSubjectList());
            model.addAttribute("isUpdate", false);
            return "dashboard/profile/company/add";
        }

        profileService.create(profileRequest, file);
        return "redirect:/company-profile/list";
    }

    @GetMapping("/go/update/{id}")
    public String updateById(@PathVariable("id") String id, Model model) {
        model.addAttribute("profile", profileService.getProfileById(id));
        model.addAttribute("subjects", subjectService.getSubjectList());
        model.addAttribute("isUpdate", true);
        return "dashboard/profile/company/add";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") String profileId,
                         @RequestParam("file") MultipartFile file,
                         @Valid @ModelAttribute ProfileRequest profileRequest,
                         BindingResult bindingResult,
                         Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("profile", profileRequest);
            model.addAttribute("subjects", subjectService.getSubjectList());
            model.addAttribute("isUpdate", true);
            return "dashboard/profile/company/add";
        }

        profileService.update(profileId, profileRequest, file);
        return "redirect:/company-profile/list";
    }

    @PostMapping("/delete")
    public String delete(@Valid @ModelAttribute ProfileDeleteDTO dto) {
        boolean result = profileService.delete(dto.getId());
        return "redirect:/company-profile/list";
    }


    private ProfileFilterDTO correctFilterDTO(String nameQuery, String phone, String role) {
        ProfileFilterDTO filterDTO = new ProfileFilterDTO();
        if (nameQuery == null || nameQuery.isBlank() || nameQuery.equals("null")) {
            filterDTO.setNameQuery(null);
        } else {
            filterDTO.setNameQuery(nameQuery);
        }

        if (phone == null || phone.isBlank() || phone.equals("null")) {
            filterDTO.setPhone(null);
        } else {
            filterDTO.setPhone(phone);
        }

        if (role == null || role.isBlank() || role.equals("null")) {
            role = "NON";
        }
        filterDTO.setRole(role);
        return filterDTO;
    }
}
