package learning.center.uz.dto.admin.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import learning.center.uz.enums.ProfileRole;
import lombok.Data;

import java.util.List;


@Data
public class ProfileRequest {
    private String id;

    @Size(min = 3)
    @NotBlank()
    private String name;

    @Size(min = 3)
    @NotBlank()
    private String surname;

    @Pattern(regexp = "^998\\d{9}$")
    @NotBlank()
    private String phone;

    @Size(min = 5)
    private String password;

    @NotNull()
    private List<ProfileRole> roles;

    private List<String> subject;

    private String photoUrl;
}
