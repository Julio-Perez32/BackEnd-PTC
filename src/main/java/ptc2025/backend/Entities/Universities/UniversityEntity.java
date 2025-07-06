package ptc2025.backend.Entities.Universities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Collate;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "UNIVERSITIES")
@Getter @Setter @ToString
@EqualsAndHashCode
public class UniversityEntity {
    @Id
    @GenericGenerator(name = "idUniversity", strategy = "guid")
    @GeneratedValue(generator = "idUniversity")
    @Column(name = "UNIVERSITYID")
    private String universityID;

    @Column(name = "UNIVERSITYNAME")
    private String universityName;

    @Column(name = "RECTOR")
    private String rector;

    @Column(name = "WEBPAGE")
    private String webPage;

}
