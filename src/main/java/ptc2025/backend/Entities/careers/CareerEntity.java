package ptc2025.backend.Entities.careers;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "CAREERS")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CareerEntity {

    @Id
    @Column(name = "CAREERID")
    private String id;

    @Column(name = "CAREERNAME", nullable = false)
    private String name;

    @Column(name = "CAREERDESCRIPTION")
    private String description;

    @Column(name = "FACULTYID")
    private String facultyId;

    @Column(name = "ISACTIVE")
    private Boolean isActive;
}
