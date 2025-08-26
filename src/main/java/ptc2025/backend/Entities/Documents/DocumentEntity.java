package ptc2025.backend.Entities.Documents;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ptc2025.backend.Entities.DocumentCategories.DocumentCategoriesEntity;

import java.time.LocalDate;

@Entity
@Table(name = "DOCUMENTS")
@Getter @Setter
@ToString(exclude = "CATEGORIES") @EqualsAndHashCode
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOCUMENTCATEGORYID", referencedColumnName = "DOCUMENTCATEGORYID")
    private DocumentCategoriesEntity categories;
}
