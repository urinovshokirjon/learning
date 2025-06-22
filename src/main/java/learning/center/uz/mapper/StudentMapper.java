package learning.center.uz.mapper;

import learning.center.uz.enums.StudentStatus;
import learning.center.uz.enums.StudyStatus;

import java.time.LocalDate;

public interface StudentMapper {
    String getId();
    String getPhotoId();
    String getName();
    String getSurname();
    String getPhone();
    StudyStatus getStatus();
    LocalDate getJoinedDate();
    LocalDate getLeftDate();
}
