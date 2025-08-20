package ptc2025.backend.Entities.Pensum;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "PENSA")
@Getter @Setter @EqualsAndHashCode @ToString
public class PensumEntity {

    @Id
    @GenericGenerator(name = "PensumID", strategy = "guid")
    @GeneratedValue(generator = "PensumID")
    @Column(name = "PENSUMID")
    private String PensumID;
    @Column(name = "CAREERID")
    private String CareerID;
    @Column(name = "VERSION")
    private String Version;
    @Column(name = "EFFECTIVEYEAR")
    private Long EffectiveYear;
}
