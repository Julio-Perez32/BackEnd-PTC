package ptc2025.backend.Entities.AcademicLevel;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "ACADEMICLEVELS")
@Getter
@Setter
@EqualsAndHashCode
public class AcademicLevelsEntity {

    @Id
    @GenericGenerator(name = "AcademicLevelID", strategy = "guid")
    @GeneratedValue(generator = "AcademicLevelID")
    @Column(name = "ACADEMICLEVELID")
    private String academicLevelID;
    @Column(name = "UNIVERSITYID")
    private String universityID;
    @Column (name = "ACADEMICLEVELNAME")
    private String academicLevelName;
}
