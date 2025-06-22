package learning.center.uz.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthDTO {
    @NotNull
    private String phone;
    @NotNull
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
}
