package learning.center.uz.controller;

import jakarta.validation.Valid;
import learning.center.uz.dto.admin.company.CompanyDTO;
import learning.center.uz.dto.admin.company.CompanyFilterDTO;
import learning.center.uz.dto.admin.profile.ProfileDTO;
import learning.center.uz.dto.admin.profile.ProfileDeleteDTO;
import learning.center.uz.service.company.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping("/list")
    public String showCompanyPage(Model model,
                                  @RequestParam(value = "name", required = false) String name,
                                  @RequestParam(value = "contact", required = false) String contact,
                                  @RequestParam(value = "owner", required = false) String owner,
                                  @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                  @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        CompanyFilterDTO filterDTO = correctFilterDTO(name, contact, owner);
        Page<CompanyDTO> result = companyService.getCompanyList(filterDTO, page, size);
        model.addAttribute("companyList", result.getContent());
        model.addAttribute("totalElements", result.getTotalElements());
        model.addAttribute("totalPages", result.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("filterDTO", filterDTO);
        model.addAttribute("pageSize", 10);
        model.addAttribute("activeLink", "company");
        return "dashboard/company/list";
    }

    @GetMapping("/go/add")
    public String goToAdd(Model model) {
        model.addAttribute("activeLink", "company");
        model.addAttribute("company", new CompanyDTO());
        List<ProfileDTO> byCompanyProfiles = companyService.getByCompanyProfiles();
        model.addAttribute("owners", byCompanyProfiles);
        model.addAttribute("isUpdate", false);
        return "dashboard/company/add";
    }

    @PostMapping("/add")
    public String create(@RequestParam("file") MultipartFile file,
                        @Valid @ModelAttribute("company") CompanyDTO dto) {
        companyService.create(dto, file);
        return "redirect:/company/list";
    }

    @GetMapping("/go/update/{id}")
    public String showUpdateForm(@PathVariable("id") String id, Model model) {
        CompanyDTO companyDTO = companyService.getById(id);
        model.addAttribute("company", companyDTO);
        model.addAttribute("isUpdate", true);
        model.addAttribute("owners", companyService.getByCompanyProfiles());
        return "dashboard/company/add";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") String companyId,
                             @RequestParam("file") MultipartFile file,
                             @Valid CompanyDTO companyDTO,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            companyDTO.setId(companyId);
            return "dashboard/company/add";
        }
        companyService.update(companyId, companyDTO, file);
        return "redirect:/company/list";
    }

    @PostMapping("/delete")
    public String delete(@Valid @ModelAttribute ProfileDeleteDTO dto) {
        boolean result = companyService.delete(dto.getId());
        return "redirect:/company/list";
    }

    private CompanyFilterDTO correctFilterDTO(String name, String contact, String owner) {
        CompanyFilterDTO filterDTO = new CompanyFilterDTO();
        if (name == null || name.isBlank() || name.equals("null")) {
            filterDTO.setName(null);
        } else {
            filterDTO.setName(name);
        }
        if (contact == null || contact.isBlank() || contact.equals("null")) {
            filterDTO.setContact(null);
        } else {
            filterDTO.setContact(contact);
        }
        if (owner == null || owner.isBlank() || owner.equals("null")) {
            filterDTO.setOwner(null);
        } else {
            filterDTO.setOwner(owner);
        }
        return filterDTO;
    }
}
