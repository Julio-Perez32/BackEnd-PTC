package ptc2025.backend.Entities.EntityType;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.Universities.UniversityEntity;


@Entity
@Table(name = "ENTITYTYPES")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class EntityTypesEntity {
    @Id
    @GenericGenerator(name = "entityTypeID", strategy = "guid")
    @GeneratedValue(generator = "entityTypeID")
    @Column(name = "ENTITYTYPEID")
    private String entityTypeID;

    @Column(name = "UNIVERSITYID")
    private String universityID;

    @Column(name = "ENTITYTYPE")
    private String entityType;

    @ManyToOne
    @JoinColumn(name = "UNIVERSITYID", referencedColumnName = "UNIVERSITYID")
    private UniversityEntity university;

}
