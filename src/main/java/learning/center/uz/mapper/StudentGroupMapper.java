package learning.center.uz.mapper;

import learning.center.uz.enums.StudyStatus;
import java.time.LocalDate;

public interface StudentGroupMapper {
    String getGroupId();
    String getGroupName();
    String getSubjectName();
    String getTeacherName();
    LocalDate getGroupStartedDate();
    LocalDate getGroupFinishedDate();
    StudyStatus getStatus();
    LocalDate getJoinedDate();
    LocalDate getLeftDate();

}
