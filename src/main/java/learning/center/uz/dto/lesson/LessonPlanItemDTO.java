package learning.center.uz.dto.lesson;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LessonPlanItemDTO {

    private String id;

    @Size(min = 2, max = 100)
    @NotBlank()
    private String title;

    @Size(min = 2, max = 100)
    @NotBlank()
    private String description;

    @Size(min = 1, max = 5)
    @NotBlank()
    private String orderNumber;

    @Size(min = 2, max = 100)
    @NotBlank()
    private String homework;

    private String lessonPlanId;

    private String lessonPlanName;

    private LocalDateTime createdDate;

    //private LocalDateTime updatedDate;

}
