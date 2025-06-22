package learning.center.uz.mapper;

import learning.center.uz.enums.StudyStatus;

import java.time.LocalDate;

public interface GroupDetailMapper {
    String getGroupName();
    LocalDate getGroupStartDate();
    LocalDate getGroupFinishedDate();
    String getTeacherName();
    String getTeacherSurname();
    String getTeacherPhone();
    String getTeacherPhotoId();
    String getStudentId();
    String getStudentName();
    String getStudentSurname();
    String getStudentPhone();
    String getStudentPhotoId();
    StudyStatus getStatus();
    LocalDate getJoinedDate();
    LocalDate getLeftDate();

    String getTeacherPhotoUrl();
    String getStudentPhotoUrl();

    default void setTeacherPhotoUrl(String teacherPhotoUrl) {
    }

    default void setStudentPhotoUrl(String teacherPhotoUrl) {
    }
}
