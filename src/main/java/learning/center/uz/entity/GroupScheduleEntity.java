package learning.center.uz.entity;

import jakarta.persistence.*;
import learning.center.uz.entity.company.CompanyEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;


@Getter
@Setter
@Entity
@Table(name = "group_schedule")
public class GroupScheduleEntity extends BaseEntity {

    @Column(name = "group_id")
    private String groupId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", insertable = false, updatable = false)
    private GroupEntity groupEntity;

    @Column(name = "day_of_week")
    private String dayOfWeek;

    @Column(name = "time")
    private LocalTime time;
}
