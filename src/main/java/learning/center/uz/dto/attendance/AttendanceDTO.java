package learning.center.uz.dto.attendance;

import com.fasterxml.jackson.annotation.JsonInclude;
import learning.center.uz.dto.group.GroupDTO;
import learning.center.uz.enums.AttendanceStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttendanceDTO {
    private String id;
    private String groupId;
    private GroupDTO group;
    private String studentId;
    private LocalDate attendanceDate;
    private AttendanceStatus status;
    private LocalDateTime updatedDate;
    private LocalDateTime createdDate;
    private Boolean visible;
}
