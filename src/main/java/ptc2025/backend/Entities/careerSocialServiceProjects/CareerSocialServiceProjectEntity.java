package ptc2025.backend.Entities.careerSocialServiceProjects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CAREERSOCIALSERVICEPROJECTS")
@Getter
@Setter
public class CareerSocialServiceProjectEntity {

    @Id
    @Column(name = "CAREERPROJECTID")
    private String id;

    @Column(name = "CAREERID", nullable = false)
    private String careerId;

    @Column(name = "SOCIALSERVICEPROJECTID", nullable = false)
    private String socialServiceProjectId;
}
