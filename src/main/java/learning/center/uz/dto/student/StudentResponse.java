package learning.center.uz.dto.student;

import learning.center.uz.enums.Gender;
import learning.center.uz.enums.StudentStatus;
import learning.center.uz.enums.StudyStatus;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class StudentResponse {
    private String id;
    private String name;
    private String surname;
    private String phone;
    private String parentPhone;
    private String photoUrl;
    private String address;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String groupId;
    private String branchId;
    private StudentStatus studentStatus;
    private String message;
    private String password;
    private LocalDateTime createdDate;
    private LocalDate joinedDate;
    private LocalDate leftDate;
    private StudyStatus studyStatus;
}
