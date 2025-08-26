package ptc2025.backend.Entities.documents;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "DOCUMENTS")
@Getter @Setter
@ToString @EqualsAndHashCode
public class DocumentEntity {

    @Id
    @Column(name = "DOCUMENTID")
    private String id;

    @Column(name = "DOCUMENTNAME", nullable = false)
    private String name;

    @Column(name = "DOCUMENTTYPE")
    private String type;

    @Column(name = "UPLOADDATE")
    private LocalDate uploadDate;

    @Column(name = "OWNERID")
    private String ownerId;

    @Column(name = "ISACTIVE")
    private Boolean isActive;
}
