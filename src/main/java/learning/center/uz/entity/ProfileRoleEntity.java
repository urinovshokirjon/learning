package learning.center.uz.entity;

import jakarta.persistence.*;
import learning.center.uz.enums.ProfileRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "profile_role")
public class ProfileRoleEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private ProfileEntity profile;
    @Column(name = "profile_id")
    private String profileId;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private ProfileRole role;
}
