package learning.center.uz.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "groups")
public class GroupEntity extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "teacher_id")
    private String teacherId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", updatable = false, insertable = false)
    private ProfileEntity teacher;

    @Column(name = "subject_id")
    private String subjectId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", updatable = false, insertable = false)
    private SubjectEntity subject;

    @Column(name = "branch_id")
    private String branchId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", updatable = false, insertable = false)
    private BranchEntity branch;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "finished_date")
    private LocalDate finishedDate;

    @Column(name = "duration")
    private Integer duration;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private List<StudentGroupEntity> students;
       // TODO Add  LessonPlan

}
