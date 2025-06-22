package learning.center.uz.dto.admin.company;
import com.fasterxml.jackson.annotation.JsonInclude;
import learning.center.uz.dto.admin.profile.ProfileDTO;
import lombok.Data;


import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanyDTO {
    private String id;
    private String name;
    private String photoId;
    private String ownerId;
    private ProfileDTO owner;
    private String contact;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean visible;
    private String photoUrl;
}
