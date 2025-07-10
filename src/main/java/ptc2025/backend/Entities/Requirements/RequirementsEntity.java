package ptc2025.backend.Entities.Requirements;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "REQUIREMENTS")
@Getter @Setter @ToString @EqualsAndHashCode
public class RequirementsEntity {
    @GenericGenerator(name = "idRequirements", strategy = "guid")
    @GeneratedValue(generator = "idRequirements")
    @Id
    @Column(name = "REQUIREMENTID")
    private String id;
    @Column(name = "UNIVERSITYID")
    private String universityID;
    @Column(name = "REQUIREMENTNAME")
    private String requirementName;
    @Column(name = "DESCRIPTION")
    private String description;
}
