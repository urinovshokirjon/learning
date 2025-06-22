package learning.center.uz.controller;

import jakarta.validation.Valid;
import learning.center.uz.dto.branch.BranchDTO;
import learning.center.uz.dto.branch.BranchDeleteDTO;
import learning.center.uz.dto.branch.BranchFilterDTO;
import learning.center.uz.service.ProfileService;
import learning.center.uz.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/branch")
public class BranchController {

    private final BranchService branchService;
    private final ProfileService profileService;

    @GetMapping("/list")
    public String getBranchList(Model model,
                                @RequestParam(value = "branchName", required = false) String branchName,
                                @RequestParam(value = "branchAddress", required = false) String branchAddress,
                                @RequestParam(value = "branchContact", required = false) String branchContact,
                                @RequestParam(value = "branchManagerQuery", required = false) String branchManagerQuery,
                                @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        BranchFilterDTO branchFilterDTO = branchFilter(branchName, branchAddress, branchContact, branchManagerQuery);
        Page<BranchDTO> result = branchService.getBranchList(branchFilterDTO, page, size);
        model.addAttribute("branchList", result.getContent());
        model.addAttribute("totalElements", result.getTotalElements());
        model.addAttribute("totalPages", result.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("filterDTO", branchFilterDTO);
        model.addAttribute("pageSize", 10);
        model.addAttribute("activeLink", "branch");
        return "dashboard/branch/list"; // go to page
    }

    @GetMapping("/go/add")
    public String goToAdd(Model model) {
        model.addAttribute("activeLink", "branch");
        model.addAttribute("branch", new BranchDTO());
        model.addAttribute("managers", profileService.getRoleCompanyManager());
        model.addAttribute("isUpdate", false);
        return "dashboard/branch/add"; // go to page
    }


    @PostMapping("/add")
    public String create(@RequestParam("file") MultipartFile file,
                         @Valid @ModelAttribute BranchDTO branchDTO,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("branch", branchDTO);
            model.addAttribute("isUpdate", false);
            return "dashboard/branch/add";
        }
        branchService.create(branchDTO, file);
        return "redirect:/branch/list"; // go to url
    }


    @GetMapping("/go/update/{id}")
    public String updateById(@PathVariable("id") String id, Model model) {
        BranchDTO branchDTO = branchService.getBranchById(id);
        model.addAttribute("branch", branchDTO);
        model.addAttribute("managers", profileService.getRoleCompanyManager());
        model.addAttribute("isUpdate", true);
        return "dashboard/branch/add"; // go to page
    }


    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") String branchId,
                         @RequestParam("file") MultipartFile file,
                         @Valid @ModelAttribute BranchDTO branchDTO,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("branch", branchDTO);
            model.addAttribute("isUpdate", true);
            return "dashboard/branch/add";
        }
        branchService.update(branchId, branchDTO, file);
        return "redirect:/branch/list"; // go to url
    }


    @PostMapping("/delete")
    public String delete(@Valid @ModelAttribute BranchDeleteDTO dto) {
        branchService.delete(dto.getId());
        return "redirect:/branch/list"; // go to url
    }


    public BranchFilterDTO branchFilter(String branchName, String branchAddress, String branchContact, String branchManagerQuery) {
        BranchFilterDTO branchFilterDTO = new BranchFilterDTO();
        if (branchName == null || branchName.isBlank() || branchName.equals("null")) {
            branchFilterDTO.setBranchName(null);
        } else {
            branchFilterDTO.setBranchName(branchName);
        }
        if (branchAddress == null || branchAddress.isBlank() || branchAddress.equals("null")) {
            branchFilterDTO.setBranchAddress(null);
        } else {
            branchFilterDTO.setBranchAddress(branchAddress);
        }
        if (branchContact == null || branchContact.isBlank() || branchContact.equals("null")) {
            branchFilterDTO.setBranchContact(null);
        } else {
            branchFilterDTO.setBranchContact(branchContact);
        }
        if (branchManagerQuery == null || branchManagerQuery.isBlank() || branchManagerQuery.equals("null")) {
            branchFilterDTO.setBranchManagerQuery(null);
        } else {
            branchFilterDTO.setBranchManagerQuery(branchManagerQuery);
        }
        return branchFilterDTO;
    }
}

