package ptc2025.backend.Entities.DegreeTypes;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "DEGREETYPES")
@Getter @Setter @ToString @EqualsAndHashCode
public class DegreeTypesEntity {
    @Id
    @GenericGenerator(name = "idDegreeTypes", strategy = "guid")
    @GeneratedValue(generator = "idDegreeTypes")
    @Column(name = "DEGREETYPEID")
    private String id;
    @Column(name = "UNIVERSITYID")
    private String universityID;
    @Column(name = "DEGREETYPENAME")
    private String degreeTypeName;
}
