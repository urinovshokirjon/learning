package learning.center.uz.dto.branch;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BranchDeleteDTO {
    @NotBlank
    private String id;
}
