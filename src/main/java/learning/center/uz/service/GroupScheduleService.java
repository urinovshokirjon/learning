package learning.center.uz.service;

import learning.center.uz.dto.group.GroupDTO;
import learning.center.uz.entity.GroupScheduleEntity;
import learning.center.uz.repository.GroupScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupScheduleService {
    private final GroupScheduleRepository groupScheduleRepository;

    public void save(String groupId, GroupDTO dto) {
        groupScheduleRepository.deleteByGroupId(groupId);
        List<GroupScheduleEntity> list = new ArrayList<>();
        if (dto.getMondaySelected()) {
            GroupScheduleEntity entity = new GroupScheduleEntity();
            entity.setGroupId(groupId);
            entity.setDayOfWeek("MONDAY");
            entity.setTime(dto.getMondayTime());
            list.add(entity);
        }
        if (dto.getTuesdaySelected()) {
            GroupScheduleEntity entity = new GroupScheduleEntity();
            entity.setGroupId(groupId);
            entity.setDayOfWeek("TUESDAY");
            entity.setTime(dto.getTuesdayTime());
            list.add(entity);
        }
        if (dto.getWednesdaySelected()) {
            GroupScheduleEntity entity = new GroupScheduleEntity();
            entity.setGroupId(groupId);
            entity.setDayOfWeek("WEDNESDAY");
            entity.setTime(dto.getWednesdayTime());
            list.add(entity);
        }
        if (dto.getThursdaySelected()) {
            GroupScheduleEntity entity = new GroupScheduleEntity();
            entity.setGroupId(groupId);
            entity.setDayOfWeek("THURSDAY");
            entity.setTime(dto.getThursdayTime());
            list.add(entity);
        }
        if (dto.getFridaySelected()) {
            GroupScheduleEntity entity = new GroupScheduleEntity();
            entity.setGroupId(groupId);
            entity.setDayOfWeek("FRIDAY");
            entity.setTime(dto.getFridayTime());
            list.add(entity);
        }
        if (dto.getSaturdaySelected()) {
            GroupScheduleEntity entity = new GroupScheduleEntity();
            entity.setGroupId(groupId);
            entity.setDayOfWeek("SATURDAY");
            entity.setTime(dto.getSaturdayTime());
            list.add(entity);
        }
        if (dto.getSundaySelected()) {
            GroupScheduleEntity entity = new GroupScheduleEntity();
            entity.setGroupId(groupId);
            entity.setDayOfWeek("SUNDAY");
            entity.setTime(dto.getSundayTime());
            list.add(entity);
        }

        groupScheduleRepository.saveAll(list);
    }

    public List<GroupScheduleEntity> getByGroupId(String groupId) {
        return groupScheduleRepository.findAllByGroupId(groupId);
    }
}
