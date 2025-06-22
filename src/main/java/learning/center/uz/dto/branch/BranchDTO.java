package learning.center.uz.dto.branch;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import learning.center.uz.dto.admin.company.CompanyDTO;
import learning.center.uz.dto.admin.profile.ProfileDTO;
import lombok.Data;
import java.time.LocalDateTime;



@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BranchDTO {

    private String id;

    @Size(min = 3)
    @NotBlank()
    private String branchName;

    @Size(min = 3)
    @NotBlank()
    private String branchAddress;

    @Pattern(regexp = "^998[0-9]{9}$")
    @NotBlank()
    private String contact;

    private String companyId;

    private CompanyDTO company;

    private String managerId;

    private String managerName;

    private String managerSurname;

    private String managerPhone;

    private ProfileDTO manager;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private Boolean visible;

    private String photoUrl;

    private String managerStr;
}
