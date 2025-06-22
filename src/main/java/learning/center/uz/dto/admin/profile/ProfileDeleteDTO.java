package learning.center.uz.dto.admin.profile;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileDeleteDTO {
    @NotBlank
    private String id;
}
