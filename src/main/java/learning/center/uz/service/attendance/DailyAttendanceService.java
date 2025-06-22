package learning.center.uz.service.attendance;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import learning.center.uz.adapter.LocalDateTypeAdapter;
import learning.center.uz.controller.DailyStudentAttendancy;
import learning.center.uz.dto.attendance.StudentAttendanceDTO;
import learning.center.uz.dto.attendance.StudentGroupAttendanceDTO;
import learning.center.uz.entity.attendance.DailyAttendanceEntity;
import learning.center.uz.enums.AttendanceStatus;
import learning.center.uz.mapper.GroupMapper;
import learning.center.uz.mapper.StudentAttendanceMapper;
import learning.center.uz.repository.DailyAttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyAttendanceService {
    private final DailyAttendanceRepository dailyAttendanceRepository;
    private final StudentAttendanceService studentAttendanceService;

    public void save(String groupId, DailyStudentAttendancy daily) {
        if (daily.getCurrentAttendanceDate() != null) {
            DailyAttendanceEntity entity = new DailyAttendanceEntity();
            entity.setGroupId(groupId);
            entity.setAttendanceDate(daily.getCurrentAttendanceDate());
            entity.setAttendanceStatus(AttendanceStatus.DONE);
            dailyAttendanceRepository.save(entity);

            studentAttendanceService.save(entity.getId(), daily.getAttendancyList());
        }
    }

    public DailyAttendanceEntity getLastDailyAttendance(String groupId) {
        return dailyAttendanceRepository.getLastDailyAttendanceByGroupId(groupId);
    }

    public List<LocalDate> getDailyAttendanceDateList(String groupId) {
        return dailyAttendanceRepository.getDailyAttendanceDateListByGroupId(groupId);
    }


    public List<StudentGroupAttendanceDTO> generateStudentGroupAttendanceList(List<StudentAttendanceMapper> studentList, List<LocalDate> dailyAttendanceHistory, int dailyAttendanceCount, Gson gson) {
        List<StudentGroupAttendanceDTO> studentGroupAttendanceDTOList = new ArrayList<>();
        for (StudentAttendanceMapper mapper : studentList) {
            StudentGroupAttendanceDTO studentGroupAttendanceDTO = new StudentGroupAttendanceDTO();
            studentGroupAttendanceDTO.setId(mapper.getId());
            studentGroupAttendanceDTO.setName(mapper.getName());
            studentGroupAttendanceDTO.setSurname(mapper.getSurname());

            if (mapper.getAttendanceJson() != null) {
                StudentAttendanceDTO[] studentAttendanceArray = gson.fromJson(mapper.getAttendanceJson(), StudentAttendanceDTO[].class);
                if (studentAttendanceArray.length < dailyAttendanceCount) {
                    List<StudentAttendanceDTO> tempList = new LinkedList<>();
                    for (int i = 0; i < dailyAttendanceCount - studentAttendanceArray.length; i++) {
                        tempList.add(null);
                    }
                    tempList.addAll(List.of(studentAttendanceArray));
                    studentGroupAttendanceDTO.setAttendanceList(tempList);
                } else {
                    studentGroupAttendanceDTO.setAttendanceList(List.of(studentAttendanceArray));
                }
            } else {
                studentGroupAttendanceDTO.setAttendanceList(new ArrayList<>());
            }
            studentGroupAttendanceDTOList.add(studentGroupAttendanceDTO);
        }
        return studentGroupAttendanceDTOList;
    }

    public DailyStudentAttendancy prepareDailyAttendance(List<StudentAttendanceMapper> studentList, LocalDate currentAttendanceDate) {
        List<StudentAttendanceDTO> currentAttendance = new LinkedList<>();
        for (StudentAttendanceMapper mapper : studentList) {
            currentAttendance.add(new StudentAttendanceDTO(mapper.getId()));
        }

        DailyStudentAttendancy dailyStudentAttendancy = new DailyStudentAttendancy();
        dailyStudentAttendancy.setAttendancyList(currentAttendance);
        dailyStudentAttendancy.setCurrentAttendanceDate(currentAttendanceDate);
        return dailyStudentAttendancy;
    }


    public List<LocalDate> calculateDates(GroupMapper group, DailyAttendanceEntity lastDailyAttendance, List<LocalDate> dailyAttendanceHistory) {
        List<LocalDate> dates;
        if (lastDailyAttendance == null) {
            dates = generateDatesBasedOnStartDate(group.getStartDate(), group.getStartDate().plusMonths(group.getDuration()), group.getLessonDays());
        } else {
            dates = generateDatesBasedOnStartDate(lastDailyAttendance.getAttendanceDate().plusDays(1), group.getStartDate().plusMonths(group.getDuration()), group.getLessonDays());
            dates.addAll(0, dailyAttendanceHistory);
        }
        return dates;
    }

    public Gson createGsonInstance() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .create();
    }


    public LocalDate calculateCurrentAttendanceDate(List<LocalDate> dates, DailyAttendanceEntity lastDailyAttendance) {
        return dates.stream()
                .filter(item -> lastDailyAttendance == null || item.isAfter(lastDailyAttendance.getAttendanceDate()))
                .findFirst()
                .orElse(null);
    }


    private List<LocalDate> generateDatesBasedOnStartDate(LocalDate startDate, LocalDate endDate, String lessonDaysOfWeek) {
        List<LocalDate> dates = new ArrayList<>();
        List<DayOfWeek> dayOfWeek = new ArrayList<>();

        LocalDate currentDate = startDate;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String[] days = lessonDaysOfWeek.split(",");
        for (String day : days) {
            dayOfWeek.add(DayOfWeek.valueOf(day));
        }

        while (!currentDate.isAfter(endDate)) {
            if (dayOfWeek.contains(currentDate.getDayOfWeek())) {
                dates.add(currentDate);
            }
            currentDate = currentDate.plusDays(1);
        }
        return dates;
    }
}
