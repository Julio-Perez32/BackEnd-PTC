package ptc2025.backend.Entities.careers;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.DegreeTypes.DegreeTypesEntity;
import ptc2025.backend.Entities.Departments.DepartmentsEntity;
import ptc2025.backend.Entities.Modalities.ModalitiesEntity;
import ptc2025.backend.Entities.Pensum.PensumEntity;
import ptc2025.backend.Entities.YearCycles.YearCyclesEntity;
import ptc2025.backend.Entities.careerCycleAvailability.CareerCycleAvailabilityEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CAREERS")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CareerEntity {

    @Id
    @Column(name = "CAREERID")
    private String id;

    @ManyToOne
    @JoinColumn(name = "ACADEMICLEVELID", referencedColumnName = "ACADEMICLEVELID")
    private AcademicLevelsEntity academicLevels;

    @ManyToOne
    @JoinColumn(name = "DEGREETYPEID", referencedColumnName = "DEGREETYPEID")
    private DegreeTypesEntity degreeTypes;

    @ManyToOne
    @JoinColumn(name = "MODALITYID", referencedColumnName = "MODALITYID")
    private ModalitiesEntity modalities;

    @ManyToOne
    @JoinColumn(name = "DEPARTMENTID", referencedColumnName = "DEPARTMENTID")
    private DepartmentsEntity departments;

    @Column(name = "CAREERNAME", nullable = false)
    private String nameCareer;

    @Column(name = "CAREERCODE")
    private String careerCode;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "MINPASSINGSCORE")
    private Integer minPassingScore;

    @Column(name = "MINMUC")
    private Integer minMUC;

    @Column(name = "COMPULSORYSUBJECTS")
    private Integer compulsorySubjects;

    @Column(name = "TOTALVALUEUNITS")
    private Integer totalValueUnits;

    @OneToMany(mappedBy = "career", cascade = CascadeType.ALL)
    private List<CareerCycleAvailabilityEntity> careerCycleAvailability = new ArrayList<>();

    @OneToMany(mappedBy = "carrer", cascade = CascadeType.ALL)
    private  List<PensumEntity> pensum = new ArrayList<>();
}
