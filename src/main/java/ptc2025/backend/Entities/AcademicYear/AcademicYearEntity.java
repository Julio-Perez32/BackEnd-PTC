package ptc2025.backend.Entities.AcademicYear;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import ptc2025.backend.Entities.FacultyDeans.FacultyDeansEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Entities.YearCycles.YearCyclesEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ACADEMICYEARS")
@Getter @Setter
@EqualsAndHashCode
public class AcademicYearEntity {
    @Id
    @GenericGenerator(name = "idAcademicYear", strategy = "guid")
    @GeneratedValue(generator = "idAcademicYear")
    @Column(name = "ACADEMICYEARID")
    private String academicYearId;


    @Column(name = "YEAR")
    @DateTimeFormat
    private Integer year;

    @Column(name = "STARTDATE")
    private LocalDate startDate;

    @Column(name = "ENDDATE")
    private LocalDate endDate;

    @Column(name = "CYCLECOUNT")
    private Integer cycleCount;

    @Column(name = "ALLOWINTERCYCLE")
    private String alowInterCycle;

    @Column(name = "DEFAULTCYCLEDURATION")
    private Integer defaultCycleDuration;

    @OneToMany(mappedBy = "ACADEMICYEAR", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<YearCyclesEntity> yearCycles = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "UNIVERSITYID", referencedColumnName = "UNIVERSITYID")
    private UniversityEntity university;

    @Override
    public String toString() {
        return "AcademicYearEntity{" +
                "academicYearId='" + academicYearId + '\'' +
                ", year=" + year +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", cycleCount=" + cycleCount +
                ", alowInterCycle='" + alowInterCycle + '\'' +
                ", defaultCycleDuration=" + defaultCycleDuration +
                ", university=" + university +
                '}';
    }
}
