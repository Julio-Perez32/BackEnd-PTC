package ptc2025.backend.Entities.Pensum;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.PensumSubject.PensumSubjectEntity;
import ptc2025.backend.Entities.careers.CareerEntity;
import ptc2025.backend.Entities.employees.EmployeeEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PENSA")
@Getter @Setter @EqualsAndHashCode
public class PensumEntity {

    @Id
    @GenericGenerator(name = "PensumID", strategy = "guid")
    @GeneratedValue(generator = "PensumID")
    @Column(name = "PENSUMID")
    private String PensumID;
    @Column(name = "VERSION")
    private String Version;
    @Column(name = "EFFECTIVEYEAR")
    private Long EffectiveYear;

    @OneToMany(mappedBy = "Pensum", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PensumSubjectEntity> PensumSubject = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "CAREERID", referencedColumnName = "CAREERID")
    private CareerEntity career;

    @Override
    public String toString() {
        return "PensumEntity{" +
                "PensumID='" + PensumID + '\'' +
                ", Version='" + Version + '\'' +
                ", EffectiveYear=" + EffectiveYear ;
    }
    //se me cerro el push bro
}
