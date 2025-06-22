package learning.center.uz.dto.group;

import learning.center.uz.enums.StudyStatus;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class StudentGroupDTO {
    private String id;
    private String studentId;
    private String groupId;
    private StudyStatus status;
    private String message;
    private LocalDate joinedDate;
    private LocalDate leftDate;

}
