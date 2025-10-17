package ptc2025.backend.Entities.Localities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.FacultyLocalities.FacultyLocalitiesEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "LOCALITIES")
@Getter @Setter
@EqualsAndHashCode
public class LocalitiesEntity {

    @Id
    @GenericGenerator(name = "idLocality", strategy = "guid")
    @GeneratedValue (generator = "idLocality")
    @Column(name = "LOCALITYID")
    private String localityID;

    // CAMBIO: Usar Integer en lugar de Boolean
    @Column(name = "ISMAINLOCALITY")
    private Integer isMainLocality; // 0 = false, 1 = true

    @Column(name = "ADDRESS")
    private String address;

    @Column (name = "PHONENUMBER")
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "UNIVERSITYID", referencedColumnName = "UNIVERSITYID")
    private UniversityEntity university;

    @OneToMany(mappedBy = "localities", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<FacultyLocalitiesEntity> facultyLocalities = new ArrayList<>();

    @Override
    public String toString() {
        return "LocalitiesEntity{" +
                "localityID='" + localityID + '\'' +
                ", isMainLocality=" + isMainLocality +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'';
    }
}