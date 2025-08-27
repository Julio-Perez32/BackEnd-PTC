package ptc2025.backend.Entities.RequirementConditions;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.Faculties.FacultiesEntity;
import ptc2025.backend.Entities.Requirements.RequirementsEntity;
import ptc2025.backend.Entities.SubjectDefinitions.SubjectDefinitionsEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "REQUIREMENTCONDITIONS")
@Getter @Setter @EqualsAndHashCode @ToString
public class RequirementConditionsEntity {

    @Id
    @GenericGenerator(name = "ConditionID", strategy = "guid")
    @GeneratedValue(generator = "ConditionID")
    @Column(name = "CONDITIONID")
    private String ConditionID;

    @ManyToOne
    @JoinColumn(name = "SUBJECTID", referencedColumnName = "SUBJECTID")
    private SubjectDefinitionsEntity subjectDefinitions;

    @ManyToOne
    @JoinColumn(name = "REQUIREMENTID", referencedColumnName = "REQUIREMENTID")
    private RequirementsEntity requirements;
}
