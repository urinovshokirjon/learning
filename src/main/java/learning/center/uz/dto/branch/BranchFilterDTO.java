package learning.center.uz.dto.branch;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;



@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BranchFilterDTO {

    private String branchName;
    private String branchAddress;
    private String branchContact;
    private String branchManagerQuery;

}
