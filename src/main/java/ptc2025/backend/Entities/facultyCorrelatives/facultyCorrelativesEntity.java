package ptc2025.backend.Entities.facultyCorrelatives;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "FACULTYCORRELATIVES")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class facultyCorrelativesEntity {
    @Id
    @GenericGenerator(name = "correlativeID", strategy = "guid")
    @GeneratedValue(generator = "correlativeID")
    @Column(name = "CORRELATIVEID")
    private String correlativeID;

    @Column(name = "FACULTYID")
    private String facultyID;

    @Column(name = "CORRELATIVENUMBER")
    private Integer correlativeNumber;
}
