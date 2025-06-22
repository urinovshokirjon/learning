package learning.center.uz.dto.group;

import learning.center.uz.dto.admin.profile.ProfileDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class GroupDTO {
    private String id;
    private String name;
    private String ownerId;
    private String branchId;
    private ProfileDTO owner;
    private Long studentCount;
    private String teacherName;
    private String teacherSurname;
    private String teacherPhone;
    private String teacherPhotoUrl;
    private String subjectName;
    private String teacherId;
    private String subjectId;

    //    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    private LocalDate finishedDate;
    private Integer duration;
    private LocalDate createdDate;
    private LocalDateTime updatedDate;
    private Boolean visible;
    private Boolean mondaySelected;
    private LocalTime mondayTime;

    private Boolean tuesdaySelected;
    private LocalTime tuesdayTime;

    private Boolean wednesdaySelected;
    private LocalTime wednesdayTime;

    private Boolean thursdaySelected;
    private LocalTime thursdayTime;

    private Boolean fridaySelected;
    private LocalTime fridayTime;

    private Boolean saturdaySelected;
    private LocalTime saturdayTime;

    private Boolean sundaySelected;
    private LocalTime sundayTime;

    public GroupDTO() {
    }

    public GroupDTO(Boolean mondaySelected, Boolean saturdaySelected, Boolean sundaySelected, Boolean thursdaySelected, Boolean tuesdaySelected, Boolean wednesdaySelected, Boolean fridaySelected) {
        this.mondaySelected = mondaySelected;
        this.saturdaySelected = saturdaySelected;
        this.sundaySelected = sundaySelected;
        this.thursdaySelected = thursdaySelected;
        this.tuesdaySelected = tuesdaySelected;
        this.wednesdaySelected = wednesdaySelected;
        this.fridaySelected = fridaySelected;
    }
}
