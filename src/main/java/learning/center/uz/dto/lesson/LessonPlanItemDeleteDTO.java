package learning.center.uz.dto.lesson;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class LessonPlanItemDeleteDTO {

    @NotBlank()
    private String id;

}
