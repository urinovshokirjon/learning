package learning.center.uz.dto.group;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GroupCreatedDTO {


    private String id;

    @NotBlank(message = "Title bo'sh bo'lishi mumkin emas")
    @Size(min = 3,  message = "Berilgan title ning uzunligi 3 ta harifdan kam bo'lishi mumkin emas")
    private String name;

    @NotBlank(message = "Title bo'sh bo'lishi mumkin emas")
    private String teacherId;

    @NotBlank(message = "Title bo'sh bo'lishi mumkin emas")
    private String subjectId;
}
