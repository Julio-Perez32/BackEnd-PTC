package ptc2025.backend.Entities.CycleTypes;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.Universities.UniversityEntity;

@Entity
@Table(name = "CYCLETYPES")
@Getter @Setter @ToString @EqualsAndHashCode
public class CycleTypesEntity {
    @Id
    @GenericGenerator(name = "IDCycleTypes", strategy = "guid")
    @GeneratedValue(generator = "IDCycleTypes")
    @Column(name = "CYCLETYPEID")
    private String id;
    @Column(name = "CYCLELABEL")
    private String cycleLabel;

    @ManyToOne
    @JoinColumn(name = "UNIVERSITYID", referencedColumnName = "UNIVERSITYID")
    private UniversityEntity university;

}
