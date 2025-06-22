package learning.center.uz.dto.admin.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import learning.center.uz.enums.ProfileRole;
import learning.center.uz.enums.ProfileStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDTO {
    private String id;
    private String name;
    private String surname;
    private String phone;
    private String password;
    private ProfileStatus status;
    private List<ProfileRole> roles;
    private String roleStr;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean visible;
    private String photoUrl;
    private List<String> subject;
    private String subjectStr;
}
