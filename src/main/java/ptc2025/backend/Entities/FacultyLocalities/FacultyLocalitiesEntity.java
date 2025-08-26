package ptc2025.backend.Entities.FacultyLocalities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "FACULTYLOCALITIES")
@Getter @Setter @ToString @EqualsAndHashCode
public class FacultyLocalitiesEntity {
    @Id
    @GenericGenerator(name = "idfacultylocalities", strategy = "guid")
    @GeneratedValue(generator = "idfacultylocalities")
    @Column(name = "FACULTYLOCALITYID")
    private String id;
    @Column(name = "FACULTYID")
    private String facultyID;
    @Column(name = "LOCALITYID")
    private String localityID;
}
