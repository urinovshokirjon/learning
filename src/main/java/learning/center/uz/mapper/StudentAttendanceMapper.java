package learning.center.uz.mapper;

import learning.center.uz.enums.StudyStatus;

public interface StudentAttendanceMapper {
    String getId();

    String getName();

    String getSurname();

    StudyStatus getStatus(); // TODO

    String getAttendanceJson();
}
