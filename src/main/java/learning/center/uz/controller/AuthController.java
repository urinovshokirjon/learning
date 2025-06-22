package learning.center.uz.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import learning.center.uz.config.CustomUserDetails;
import learning.center.uz.dto.auth.AuthDTO;
import learning.center.uz.service.AuthService;
import learning.center.uz.util.SpringSecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @GetMapping("/goToLoginPage")
    public String goToLoginPage(Model model) {
        model.addAttribute("isError", new AuthDTO());
        return "auth/loginPage";
    }


    @GetMapping("/loginSuccess")
    public String loginSuccess(Model model) {
        CustomUserDetails userDetails = SpringSecurityUtil.getCurrentUserDetail();
        assert userDetails != null;
        model.addAttribute("activeLink", "dashboard");
        return "redirect:/dashboard";
    }


    @GetMapping(value="/logout")
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Logout");
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/auth/goToLoginPage?logout";
    }

}
