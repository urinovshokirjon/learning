package learning.center.uz.dto.student;


import lombok.Data;

@Data
public class StudentFilterDTO {
    private String nameQuery;
    private String phone;
    private String studyStatus;
    private String gender;
}
