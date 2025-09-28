package ptc2025.backend.Entities.EvaluationInstruments;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
//se borro lo mas probable
@Entity
@Table(name = "EVALUATIONINSTRUMENTS")
@ToString @EqualsAndHashCode @Getter @Setter
public class EvaluationInstrumentsEntity {

    @Id
    @GenericGenerator(name = "instrumentID", strategy = "guid")
    @GeneratedValue(generator = "instrumentID")
    @Column(name = "INSTRUMENTID")
    private String instrumentID;
    @Column(name = "INSTRUMENTTYPEID")
    private String instrumentTypeID;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "USESRUBRIC")
    private Character usesRubric;
}
