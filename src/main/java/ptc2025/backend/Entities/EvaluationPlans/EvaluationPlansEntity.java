package ptc2025.backend.Entities.EvaluationPlans;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.CourseOfferings.CourseOfferingsEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;

import java.time.LocalDate;

@Entity
@Table(name = "EVALUATIONPLANS")
@Getter
@Setter
@ToString
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
    @Column(name = "CREATEDAT")
    private LocalDate createdAt;



    @ManyToOne
    @JoinColumn(name = "COURSEOFFERINGID", referencedColumnName = "COURSEOFFERINGID")
    private CourseOfferingsEntity courseOfferings;

}
/**EVALUATIONPLANID
 COURSEOFFERINGID
 PLANNAME
 DESCRIPTION
 CREATEDAT*/