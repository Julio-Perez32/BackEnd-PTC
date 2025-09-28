package ptc2025.backend.Entities.CourseOfferings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Controller.CourseOfferingSchedules.CourseOfferingSchedulesController;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.AcademicYear.AcademicYearEntity;
import ptc2025.backend.Entities.CourseOfferingSchedules.CourseOfferingSchedulesEntity;
import ptc2025.backend.Entities.CourseOfferingsTeachers.CourseOfferingsTeachersEntity;
import ptc2025.backend.Entities.CycleTypes.CycleTypesEntity;
import ptc2025.backend.Entities.DegreeTypes.DegreeTypesEntity;
import ptc2025.backend.Entities.EvaluationPlans.EvaluationPlansEntity;
import ptc2025.backend.Entities.SubjectDefinitions.SubjectDefinitionsEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Entities.YearCycles.YearCyclesEntity;
import ptc2025.backend.Entities.courseEnrollments.CourseEnrollmentEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "COURSEOFFERINGS")
@Getter @Setter @EqualsAndHashCode
public class CourseOfferingsEntity {

    @Id
    @GenericGenerator(name = "CourseOfferingID", strategy = "guid")
    @GeneratedValue(generator = "CourseOfferingID")
    @Column(name = "COURSEOFFERINGID")
    private String CourseOfferingID;

    @OneToMany(mappedBy = "courseOfferings", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<EvaluationPlansEntity> evaluationPlans = new ArrayList<>();

    @OneToMany(mappedBy = "courseOfferings", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CourseEnrollmentEntity> courseEnrollment = new ArrayList<>();

    @OneToMany(mappedBy = "courseOfferings", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CourseOfferingSchedulesEntity> courseOfferingSchedules = new ArrayList<>();

    @OneToMany(mappedBy = "courseOfferings", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CourseOfferingsTeachersEntity> courseOfferingsTeachers = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "SUBJECTID", referencedColumnName = "SUBJECTID")
    private SubjectDefinitionsEntity subjectDefinitions;

    @ManyToOne
    @JoinColumn(name = "YEARCYCLEID", referencedColumnName = "YEARCYCLEID")
    private YearCyclesEntity yearCycles;

    @Override
    public String toString() {
        return "CourseOfferingsEntity{" +
                "CourseOfferingID='" + CourseOfferingID + '\'' +
                ", subjectDefinitions=" + subjectDefinitions +
                ", yearCycles=" + yearCycles +
                '}';
    }
}
