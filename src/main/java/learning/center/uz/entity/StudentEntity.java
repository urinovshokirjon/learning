package learning.center.uz.entity;

import jakarta.persistence.*;
import learning.center.uz.entity.company.CompanyEntity;
import learning.center.uz.enums.Gender;
import learning.center.uz.enums.StudentStatus;
import learning.center.uz.enums.StudyStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "student")
@DynamicUpdate
public class StudentEntity extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "phone")
    private String phone;

    @Column(name = "parent_phone")
    private String parentPhone;

    @Column(name = "photo_id")
    private String photoId;
    @JoinColumn(name = "photo_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private AttachEntity photo;

    @Column(name = "password")
    private String password;

    @Column(name = "address")
    private String address;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "student_status")
    private StudentStatus studentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "study_status")
    private StudyStatus studyStatus;

    @Column(name = "message")
    private String message;

    @Column(name = "deleted_id")
    private String deletedId;

    @Column(name = "company_id")
    private String companyId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", insertable = false, updatable = false)
    private CompanyEntity company;

    @Column(name = "branch_id")
    private String branchId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", insertable = false, updatable = false)
    private BranchEntity branch;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<StudentGroupEntity> groups;
}