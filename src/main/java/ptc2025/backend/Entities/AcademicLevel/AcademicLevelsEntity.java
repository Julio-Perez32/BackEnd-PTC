package ptc2025.backend.Entities.AcademicLevel;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.Universities.UniversityEntity;

@Entity
@Table(name = "ACADEMICLEVELS")
@Getter @Setter @EqualsAndHashCode @ToString
public class AcademicLevelsEntity {

    @Id
    @GenericGenerator(name = "AcademicLevelID", strategy = "guid")
    @GeneratedValue(generator = "AcademicLevelID")
    @Column(name = "ACADEMICLEVELID")
    private String academicLevelID;
    @Column (name = "ACADEMICLEVELNAME")
    private String academicLevelName;

    /**
     * Aca se define que el atributo university es de tipo UniversityEntity
     * con JoinColumn(name -> apunta hacia la llave foranea
     * referencedColumnName -> apunta hacia la llave primaria de la tabla Universities*/
    @ManyToOne
    @JoinColumn(name = "UNIVERSITYID", referencedColumnName = "UNIVERSITYID")
    private UniversityEntity university;
}
