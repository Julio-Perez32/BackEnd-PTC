package ptc2025.backend.Entities.RequirementConditions;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "REQUIREMENTCONDITIONS")
@Getter @Setter @EqualsAndHashCode @ToString
public class RequirementConditionsEntity {

    @Id
    @GenericGenerator(name = "ConditionID", strategy = "guid")
    @GeneratedValue(generator = "ConditionID")
    @Column(name = "CONDITIONID")
    private String ConditionID;
    @Column(name = "REQUIREMENTID")
    private String RequirementID;
    @Column(name = "SUBJECTID")
    private String SubjectID;
}
