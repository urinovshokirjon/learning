package learning.center.uz.entity;

import jakarta.persistence.*;
import learning.center.uz.entity.company.CompanyEntity;
import learning.center.uz.enums.ProfileStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "profile")
public class ProfileEntity extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "phone")
    private String phone;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private ProfileStatus status;

    @Column(name = "photo_id")
    private String photoId;
    @JoinColumn(name = "photo_id", insertable = false, updatable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private AttachEntity photo;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY)
    private List<ProfileRoleEntity> profileRoles;

    @Column(name = "deleted_date")
    private LocalDateTime deletedDate;
    @Column(name = "deleted_id")
    private String deletedId;

    @Column(name = "company_id")
    private String companyId;
    @JoinColumn(name = "company_id", insertable = false, updatable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private CompanyEntity company;

}
