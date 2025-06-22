package learning.center.uz.entity.company;

import jakarta.persistence.*;
import learning.center.uz.entity.AttachEntity;
import learning.center.uz.entity.BaseEntity;
import learning.center.uz.entity.ProfileEntity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "company")
public class CompanyEntity extends BaseEntity {

    @Column(name = "name")
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner", insertable = false, updatable = false)
    private ProfileEntity owner;
    @Column(name = "owner")
    private String ownerId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo", insertable = false, updatable = false)
    private AttachEntity photo;
    @Column(name = "photo")
    private String photoId;

    @Column(name = "contact")
    private String contact;

    @Column(name = "deleted_id")
    private String deletedId;
}
