package ptc2025.backend.Entities.EntityType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.CodeGenerators.CodeGeneratorsEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Entities.Users.UsersEntity;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "ENTITYTYPES")
@Getter
@Setter
@EqualsAndHashCode
public class EntityTypesEntity {
    @Id
    @GenericGenerator(name = "entityTypeID", strategy = "guid")
    @GeneratedValue(generator = "entityTypeID")
    @Column(name = "ENTITYTYPEID")
    private String entityTypeID;

    @Column(name = "ENTITYTYPE")
    private String entityType;

    @ManyToOne
    @JoinColumn(name = "UNIVERSITYID", referencedColumnName = "UNIVERSITYID")
    private UniversityEntity university;

    @OneToMany(mappedBy = "entityType", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CodeGeneratorsEntity> codeGenerators = new ArrayList<>();

    @Override
    public String toString() {
        return "EntityTypesEntity{" +
                "entityTypeID='" + entityTypeID + '\'' +
                ", entityType='" + entityType + '\'' +
                ", university=" + university +
                ", codeGenerators=" + codeGenerators +
                '}';
    }
}
