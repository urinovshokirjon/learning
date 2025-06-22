package learning.center.uz.controller;

import learning.center.uz.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class ProfileDashboard {

    @Autowired
    private ProfileService profileService;

    @GetMapping({"", "/"})
    public String goToDashboard(Model model) {
        model.addAttribute("activeLink", "dashboard");
//        model.addAttribute("profileService", profileService);
        return "dashboard/dashboard/index";
    }
}
