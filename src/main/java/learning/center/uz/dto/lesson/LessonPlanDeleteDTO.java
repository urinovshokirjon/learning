package learning.center.uz.dto.lesson;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LessonPlanDeleteDTO {

    @NotBlank
    private String id;

}
