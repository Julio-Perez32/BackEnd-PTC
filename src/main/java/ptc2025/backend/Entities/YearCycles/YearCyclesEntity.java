package ptc2025.backend.Entities.YearCycles;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.AcademicYear.AcademicYearEntity;
import ptc2025.backend.Entities.CourseOfferings.CourseOfferingsEntity;
import ptc2025.backend.Entities.CycleTypes.CycleTypesEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Entities.careerCycleAvailability.CareerCycleAvailabilityEntity;
import ptc2025.backend.Entities.studentCycleEnrollments.StudentCycleEnrollmentEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "YEARCYCLES")
@Getter @Setter @ToString @EqualsAndHashCode
public class YearCyclesEntity {

    @Id
    @GenericGenerator(name = "YEARCYCLEID", strategy = "guid")
    @GeneratedValue(generator = "YEARCYCLEID")
    @Column(name = "YEARCYCLEID")
    private String id;
    @Column(name = "STARTDATE")
    private LocalDate startDate;
    @Column(name = "ENDDATE")
    private LocalDate endDate;


    @OneToMany(mappedBy = "yearCycles", cascade = CascadeType.ALL)
    private List<CareerCycleAvailabilityEntity> careerCycleAvailability = new ArrayList<>();

    @OneToMany(mappedBy = "yearCycles", cascade = CascadeType.ALL)
    private List<StudentCycleEnrollmentEntity> studentCycleEnrollment = new ArrayList<>();

    @OneToMany(mappedBy = "yearCycles", cascade = CascadeType.ALL)
    private List<CourseOfferingsEntity> courseOfferings = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "CYCLETYPEID", referencedColumnName = "CYCLETYPEID")
    private CycleTypesEntity cycleTypes;

    @ManyToOne
    @JoinColumn(name = "ACADEMICYEARID", referencedColumnName = "ACADEMICYEARID")
    private AcademicYearEntity ACADEMICYEAR;

}
