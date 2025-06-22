package learning.center.uz.dto.subject;

import lombok.Data;
import java.time.LocalDateTime;


@Data
public class SubjectDTO {
    private String id;
    private String name;
    private Integer groupCount;
    private String companyId;
    private LocalDateTime createdDate;
}
