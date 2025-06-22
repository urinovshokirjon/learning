package learning.center.uz.dto.lesson;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LessonPlanDTO {

    private String id;

    @Size(min = 5, max = 50)
    @NotBlank()
    private String lessonPlanName;

    private String subjectId;

    private String subjectName;

    private LocalDateTime createdDate;

}
