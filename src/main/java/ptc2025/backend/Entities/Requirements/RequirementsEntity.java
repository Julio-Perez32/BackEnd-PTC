package ptc2025.backend.Entities.Requirements;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.Universities.UniversityEntity;

@Entity
@Table(name = "REQUIREMENTS")
@Getter @Setter @ToString
public class RequirementsEntity {
    @GenericGenerator(name = "idRequirements", strategy = "guid")
    @GeneratedValue(generator = "idRequirements")
    @Id
    @Column(name = "REQUIREMENTID")
    private String id;
    @Column(name = "REQUIREMENTNAME")
    private String requirementName;
    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne
    @JoinColumn(name = "UNIVERSITYID", referencedColumnName = "UNIVERSITYID")
    private UniversityEntity university;

    @Override
    public String toString() {
        return "RequirementsEntity{" +
                "id='" + id + '\'' +
                ", requirementName='" + requirementName + '\'' +
                ", description='" + description ;
    }
}
