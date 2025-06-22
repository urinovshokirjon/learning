package learning.center.uz.repository;


import jakarta.transaction.Transactional;
import learning.center.uz.entity.GroupScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface GroupScheduleRepository extends JpaRepository<GroupScheduleEntity, String> {

    List<GroupScheduleEntity> findAllByGroupId(String groupId);

    @Transactional
    @Modifying
    void deleteByGroupId(String groupId);
}
