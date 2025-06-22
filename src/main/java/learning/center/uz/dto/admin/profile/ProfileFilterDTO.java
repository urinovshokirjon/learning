package learning.center.uz.dto.admin.profile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileFilterDTO {
    private String nameQuery;
    private String phone;
    private String role = "NON";
}
