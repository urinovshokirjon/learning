package learning.center.uz.mapper;
import java.time.LocalDate;


public interface GroupMapper {
    String getId();
    String getName();
    LocalDate getStartDate();
    Integer getDuration();
    String getTeacherName();
    String getTeacherSurname();
    String getTeacherPhone();
    String getSubjectName();
    String getLessonDays();
    Integer getStudentCount();
}
