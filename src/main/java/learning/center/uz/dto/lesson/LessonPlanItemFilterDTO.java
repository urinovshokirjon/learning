package learning.center.uz.dto.lesson;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LessonPlanItemFilterDTO {

    private String title;
    private String description;
    private String orderNumber;
    private String homework;
    private LocalDateTime createdDate;
    private String lessonPlanId;
    private String lessonPlanName;
}
