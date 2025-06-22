package learning.center.uz.entity;


import jakarta.persistence.*;
import learning.center.uz.entity.company.CompanyEntity;
import lombok.Data;

@Data
@Entity
@Table(name = "lesson_plan")
public class LessonPlanEntity extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "subject_id")
    private String subjectId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", insertable = false, updatable = false)
    private SubjectEntity subject;

    @Column(name = "company_id")
    private String companyId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", insertable = false, updatable = false)
    private CompanyEntity company;

    @Column(name = "deleted_id")
    private String deletedId;

}
