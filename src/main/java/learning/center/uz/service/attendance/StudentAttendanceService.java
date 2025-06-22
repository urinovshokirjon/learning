package learning.center.uz.service.attendance;

import learning.center.uz.dto.attendance.StudentAttendanceDTO;
import learning.center.uz.entity.attendance.StudentAttendanceEntity;
import learning.center.uz.mapper.StudentAttendanceMapper;
import learning.center.uz.repository.StudentAttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentAttendanceService {
    private final StudentAttendanceRepository studentAttendanceRepository;

    public void save(String dailyAttendanceId, List<StudentAttendanceDTO> attendancyList) {
        List<StudentAttendanceEntity> entityList = new ArrayList<>();
        for (StudentAttendanceDTO studentAttendanceDTO : attendancyList) {
            StudentAttendanceEntity entity = new StudentAttendanceEntity();
            entity.setDailyAttendanceId(dailyAttendanceId);
            entity.setStudentId(studentAttendanceDTO.getId());
            entity.setCame(studentAttendanceDTO.getCame());
            entity.setHomeworkStatus(studentAttendanceDTO.getHomeworkStatus());
            entityList.add(entity);
        }
        studentAttendanceRepository.saveAll(entityList);
    }

    public List<StudentAttendanceMapper> getStudentAttendanceByGroupId(String groupId) {
        return studentAttendanceRepository.getStudentAttendanceByGroupId(groupId);
    }
}
