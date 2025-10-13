package ptc2025.backend.Entities.EvaluationPlans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.CourseOfferings.CourseOfferingsEntity;
import ptc2025.backend.Entities.People.PeopleEntity;
import ptc2025.backend.Entities.PlanComponents.PlanComponentsEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "EVALUATIONPLANS")
@Getter
@Setter

@EqualsAndHashCode
public class EvaluationPlansEntity {
    @Id
    @GenericGenerator(name = "evaluationPlanID", strategy = "guid")
    @GeneratedValue(generator = "evaluationPlanID")
    @Column(name = "EVALUATIONPLANID")
    private String evaluationPlanID;
    @Column(name = "PLANNAME")
    private String planName;
    @Column(name ="DESCRIPTION" )
    private String description;
    @Column(name = "CREATEDAT", nullable = false)
    private LocalDate createdAt;

    @OneToMany
    @JsonIgnore
    private List<PlanComponentsEntity> evaluationPlans = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "COURSEOFFERINGID", referencedColumnName = "COURSEOFFERINGID")
    private CourseOfferingsEntity courseOfferings;


    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDate.now(); // <-- CLAVE: evita ORA-01400
    }


    @Override
    public String toString() {
        return "EvaluationPlansEntity{" +
                "evaluationPlanID='" + evaluationPlanID + '\'' +
                ", planName='" + planName + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", courseOfferings=" + courseOfferings +
                '}';
    }
}
/**EVALUATIONPLANID
 COURSEOFFERINGID
 PLANNAME
 DESCRIPTION
 CREATEDAT*/