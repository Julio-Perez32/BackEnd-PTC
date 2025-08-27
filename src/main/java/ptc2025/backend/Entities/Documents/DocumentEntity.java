package ptc2025.backend.Entities.Documents;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.DocumentCategories.DocumentCategoriesEntity;
import ptc2025.backend.Entities.People.PeopleEntity;
import ptc2025.backend.Entities.StudentDocument.StudentDocumentsEntity;
import ptc2025.backend.Entities.Students.StudentsEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "DOCUMENTS")
@Getter @Setter
@ToString(exclude = "CATEGORIES") @EqualsAndHashCode
public class DocumentEntity {

    @Id
    @GenericGenerator(name = "DOCUMENTID", strategy = "guid")
    @GeneratedValue(generator = "DOCUMENTID")
    @Column(name = "DOCUMENTID")
    private String id;
    @Column(name = "DOCUMENTNAME", nullable = false)
    private String name;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
    private List<StudentDocumentsEntity> studentDocuments = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "DOCUMENTCATEGORYID", referencedColumnName = "DOCUMENTCATEGORYID")
    private DocumentCategoriesEntity documentCategory;

}
