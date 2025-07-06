package ptc2025.backend.Entities.cycleTypes;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "CYCLETYPES")
@Getter @Setter @ToString @EqualsAndHashCode
public class cycleTypesEntity {
    @Id
    @GenericGenerator(name = "IDCycleTypes", strategy = "guid")
    @GeneratedValue(generator = "IDCycleTypes")
    @Column(name = "CYCLETYPEID")
    private String id;
    @Column(name = "UNIVERSITYID")
    private String universityID;
    @Column(name = "CYCLELABEL")
    private String cycleLabel;

}
