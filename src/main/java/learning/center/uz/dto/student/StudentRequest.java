package learning.center.uz.dto.student;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import learning.center.uz.enums.Gender;
import learning.center.uz.enums.StudentStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentRequest {
    private String id;

    @Size(min = 2, max = 50)
    @NotBlank()
    private String name;

    @Size(min = 2, max = 50)
    @NotBlank()
    private String surname;

    @Pattern(regexp = "^998\\d{9}$")
    private String phone;

    @Pattern(regexp = "^998\\d{9}$")
    private String parentPhone;

    private String photoUrl;

    private String password;

    private Gender gender;

    private String address;

    private String branchId;

    private LocalDate dateOfBirth;

}
