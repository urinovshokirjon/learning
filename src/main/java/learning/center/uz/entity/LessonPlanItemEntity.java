package learning.center.uz.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "lesson_plan_item")
public class LessonPlanItemEntity extends BaseEntity {

    @Column(name = "title", columnDefinition = "text")
    private String title;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "homework", columnDefinition = "text")
    private String homework;

    @Column(name = "lesson_plan_id")
    private String lessonPlanId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_plan_id", insertable = false, updatable = false)
    private LessonPlanEntity lessonPlan;

    @Column(name = "deleted_id")
    private String deletedId;

}
