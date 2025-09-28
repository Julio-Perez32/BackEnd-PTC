package ptc2025.backend.Entities.PlanComponents;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.EvaluationPlans.EvaluationPlansEntity;
import ptc2025.backend.Entities.People.PeopleEntity;
import ptc2025.backend.Entities.StudentEvaluations.StudentEvaluationsEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "EVALUATIONPLANCOMPONENTS")
@Getter
@Setter
@EqualsAndHashCode
public class PlanComponentsEntity {
    @Id
    @GenericGenerator(name = "idInstrument", strategy = "guid")
    @GeneratedValue(generator = "idInstrument")
    @Column(name = "COMPONENTID")
    private String componentID;

    @Column(name = "RUBRIC")
    private String rubric;

    @Column(name = "COMPONENTNAME")
    private String componentName;

    @Column(name = "WEIGHTPERCENTAGE")
    private Double weightPercentage;

    @Column(name = "ORDERINDEX")
    private Integer orderIndex = 1;

    @ManyToOne
    @JoinColumn(name = "EVALUATIONPLANID", referencedColumnName = "EVALUATIONPLANID")
    private EvaluationPlansEntity evaluationPlans;

    @OneToMany(mappedBy = "planComponents", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<StudentEvaluationsEntity> PlanComponents = new ArrayList<>();

    @Override
    public String toString() {
        return "PlanComponentsEntity{" +
                "componentID='" + componentID + '\'' +
                ", rubric='" + rubric + '\'' +
                ", componentName='" + componentName + '\'' +
                ", weightPercentage=" + weightPercentage +
                ", orderIndex=" + orderIndex;
    }
    //se me cerro el push bro
}
