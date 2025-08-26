package ptc2025.backend.Entities.FacultyLocalities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.Faculties.FacultiesEntity;
import ptc2025.backend.Entities.Localities.LocalitiesEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;

@Entity
@Table(name = "FACULTYLOCALITIES")
@Getter @Setter @ToString @EqualsAndHashCode
public class FacultyLocalitiesEntity {
    @Id
    @GenericGenerator(name = "idfacultylocalities", strategy = "guid")
    @GeneratedValue(generator = "idfacultylocalities")
    @Column(name = "FACULTYLOCALITYID")
    private String id;


    @ManyToOne
    @JoinColumn(name = "FACULTYID", referencedColumnName = "FACULTYID")
    private FacultiesEntity faculty;

    @ManyToOne
    @JoinColumn(name = "LOCALITYID", referencedColumnName = "LOCALITYID")
    private LocalitiesEntity localities;
}
