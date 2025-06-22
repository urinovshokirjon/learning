package learning.center.uz.dto.lesson;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LessonPlanFilterDTO {

    private String lessonPlanId;
    private String lessonPlanName;
    private String subjectName;
    private String subjectId;
    private String createdDate;

}
