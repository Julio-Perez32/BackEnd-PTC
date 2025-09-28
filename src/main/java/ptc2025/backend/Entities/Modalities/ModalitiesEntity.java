package ptc2025.backend.Entities.Modalities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.Universities.UniversityEntity;

@Entity
@Table(name = "MODALITIES")
@Getter @Setter @EqualsAndHashCode
public class ModalitiesEntity {

    @Id
    @GenericGenerator(name = "idModalities", strategy = "guid")
    @GeneratedValue(generator = "idModalities")
    @Column(name = "MODALITYID")
    private String id;
    @Column(name = "MODALITYNAME")
    private String modalityName;

    @ManyToOne
    @JoinColumn(name = "UNIVERSITYID", referencedColumnName = "UNIVERSITYID")
    private UniversityEntity university;

    @Override
    public String toString() {
        return "ModalitiesEntity{" +
                "id='" + id + '\'' +
                ", modalityName='" + modalityName + '\'' ;
    }
    //se me cerro el push bro
}
