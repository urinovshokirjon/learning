package learning.center.uz.util;


import learning.center.uz.config.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SpringSecurityUtil {
    public static CustomUserDetails getCurrentUserDetail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal().equals("anonymousUser")) {
            return null;
        }
        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        return details;
    }

    public static String getCurrentUserId() {
        return getCurrentUserDetail().getId();
    }

    public static String getCurrenUserPhoto() {
        return getCurrentUserDetail().getPhotoUrl();
    }
}
