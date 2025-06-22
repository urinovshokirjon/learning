package learning.center.uz.dto.student;

import learning.center.uz.enums.StudyStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentUpdateStatusDTO {
    private String id;
    private String studentId;
    private StudyStatus status;
    private String message;
}
