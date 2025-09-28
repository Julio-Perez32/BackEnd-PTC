package ptc2025.backend.Entities.Documents;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@EqualsAndHashCode
public class DocumentEntity {

    @Id
    @GenericGenerator(name = "DOCUMENTID", strategy = "guid")
    @GeneratedValue(generator = "DOCUMENTID")
    @Column(name = "DOCUMENTID")
    private String id;
    @Column(name = "DOCUMENTNAME", nullable = false)
    private String name;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<StudentDocumentsEntity> studentDocuments = new ArrayList<>();

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "DOCUMENTCATEGORYID", referencedColumnName = "DOCUMENTCATEGORYID")
    private DocumentCategoriesEntity documentCategory;

    @Override
    public String toString() {
        return "DocumentEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
