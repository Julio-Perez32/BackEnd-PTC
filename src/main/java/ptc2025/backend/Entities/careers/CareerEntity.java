package ptc2025.backend.Entities.careers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.DegreeTypes.DegreeTypesEntity;
import ptc2025.backend.Entities.Departments.DepartmentsEntity;
import ptc2025.backend.Entities.Modalities.ModalitiesEntity;
import ptc2025.backend.Entities.Pensum.PensumEntity;
import ptc2025.backend.Entities.YearCycles.YearCyclesEntity;
import ptc2025.backend.Entities.careerCycleAvailability.CareerCycleAvailabilityEntity;
import ptc2025.backend.Entities.careerSocialServiceProjects.CareerSocialServiceProjectEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CAREERS")
@Getter
@Setter
@EqualsAndHashCode
public class CareerEntity {

    @Id
    @GenericGenerator(name = "careerGenerator", strategy = "guid")
    @GeneratedValue(generator = "careerGenerator")
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
    @JsonIgnore
    private List<CareerCycleAvailabilityEntity> careerCycleAvailability = new ArrayList<>();

    @OneToMany(mappedBy = "career", cascade = CascadeType.ALL)
    @JsonIgnore
    private  List<PensumEntity> pensum = new ArrayList<>();

    @OneToMany(mappedBy = "career", cascade = CascadeType.ALL)
    @JsonIgnore
    private  List<CareerSocialServiceProjectEntity> socialServiceProjects = new ArrayList<>();

    @Override
    public String toString() {
        return "CareerEntity{" +
                "id='" + id + '\'' +
                ", academicLevels=" + academicLevels +
                ", degreeTypes=" + degreeTypes +
                ", modalities=" + modalities +
                ", departments=" + departments +
                ", nameCareer='" + nameCareer + '\'' +
                ", careerCode='" + careerCode + '\'' +
                ", description='" + description + '\'' +
                ", minPassingScore=" + minPassingScore +
                ", minMUC=" + minMUC +
                ", compulsorySubjects=" + compulsorySubjects +
                ", totalValueUnits=" + totalValueUnits +
                '}';
    }
}
