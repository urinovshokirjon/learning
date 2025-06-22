package learning.center.uz.mapper;


import java.time.LocalDateTime;

public interface LessonPlanItemMapperI {

    String getId();
    String getTitle();
    String getDescription();
    String getOrderNumber();
    String getHomework();
    LocalDateTime getCreatedDate();
}
