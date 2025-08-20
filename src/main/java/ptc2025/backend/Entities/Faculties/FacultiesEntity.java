package ptc2025.backend.Entities.Faculties;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "FACULTIES")
@Getter @Setter @EqualsAndHashCode @ToString
public class FacultiesEntity {

    @Id
    @GenericGenerator(name = "facultyID", strategy = "guid")
    @GeneratedValue(generator = "facultyID")
    @Column(name = "FACULTYID")
    private String facultyID;
    @Column(name = "FACULTYCODE")
    private String facultyCode;
    @Column(name = "CONTACTPHONE")
    private String contactPhone;
    @Column(name = "CORRELATIVECODE")
    private String correlativeCode;
}
