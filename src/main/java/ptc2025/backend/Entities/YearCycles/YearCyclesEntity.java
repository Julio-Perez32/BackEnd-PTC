package ptc2025.backend.Entities.YearCycles;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;

@Entity
@Table(name = "YEARCYCLES")
@Getter @Setter @ToString @EqualsAndHashCode
public class YearCyclesEntity {

    @Id
    @GenericGenerator(name = "YEARCYCLEID", strategy = "guid")
    @GeneratedValue(generator = "YEARCYCLEID")
    @Column(name = "YEARCYCLEID")
    private String id;
    @Column(name = "ACADEMICYEARID")
    private String academicYearID;
    @Column(name = "CYCCLETYPEID")
    private String cycleTypeID;
    @Column(name = "STARTDATE")
    private LocalDate startDate;
    @Column(name = "ENDDATE")
    private LocalDate endDate;
}
