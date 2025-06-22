package learning.center.uz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/test")
public class TestController {

    @GetMapping("/list")
    public String getProfileList() {
        return "It is working";
    }

    @GetMapping("/adminkit")
    public String adminkitTest() {
        return "temp/new-theme-empty-page";
    }

    @GetMapping("/advanced-inputs")
    public String goToAdvancedInputs() {
        return "temp/advance-input";
    }
}
