package learning.center.uz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin-dashboard")
public class AdminDashboard {
    @GetMapping({"", "/"})
    public String goToDashboard(Model model) {
        model.addAttribute("activeLink","dashboard");
        return "dashboard/dashboard/index";
    }
}
