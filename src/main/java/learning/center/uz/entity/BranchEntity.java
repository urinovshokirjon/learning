package learning.center.uz.entity;
import jakarta.persistence.*;
import learning.center.uz.entity.company.CompanyEntity;
import lombok.Data;

import java.util.List;


@Data
@Entity
@Table(name = "branch")
public class BranchEntity extends BaseEntity{

    @Column(name = "name")
    private String name;

    @Column(name = "company_id")
    private String companyId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", insertable = false, updatable = false)
    private CompanyEntity company;

    @Column(name = "branch_address")
    private String address;

    @Column(name = "contact")
    private String contact;

    @Column(name = "photo_id")
    private String photoId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id", insertable = false, updatable = false)
    private AttachEntity photo;

    @Column(name = "manager_id")
    private String managerId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", insertable = false, updatable = false)
    private ProfileEntity manager;

    @Column(name = "deleted_id")
    private String deletedId;

    @OneToMany(mappedBy = "branch", fetch = FetchType.LAZY)
    private List<GroupEntity> groups;

}
