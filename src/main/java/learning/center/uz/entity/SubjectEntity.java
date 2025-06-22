package learning.center.uz.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "subject")
public class SubjectEntity extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "company_id")
    private String companyId;
}
