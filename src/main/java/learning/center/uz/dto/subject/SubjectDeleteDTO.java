package learning.center.uz.dto.subject;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubjectDeleteDTO {
    @NotBlank
    private String id;
}
