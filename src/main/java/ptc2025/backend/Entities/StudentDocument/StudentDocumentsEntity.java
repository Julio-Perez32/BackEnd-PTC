package ptc2025.backend.Entities.StudentDocument;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.Documents.DocumentEntity;
import ptc2025.backend.Entities.Requirements.RequirementsEntity;
import ptc2025.backend.Entities.Students.StudentsEntity;
import ptc2025.backend.Entities.SubjectDefinitions.SubjectDefinitionsEntity;

import java.time.LocalDate;

@Entity
@Table(name = "STUDENTDOCUMENTS")
@Getter
@Setter
@EqualsAndHashCode
public class StudentDocumentsEntity {
    @Id
    @GenericGenerator(name = "studentDocumentID", strategy = "guid")
    @GeneratedValue(generator = "studentDocumentID")
    @Column(name = "STUDENTDOCUMENTID")
    private String studentDocumentID;

    @Column(name = "SUBMITTED")
    private Character submitted;

    @Column(name = "SUBMISSIONDATE")
    private LocalDate submissionDate;

    @Column(name = "VERIFIED")
    private Character verified;

    @ManyToOne
    @JoinColumn(name = "STUDENTID", referencedColumnName = "STUDENTID")
    private StudentsEntity students;

    @ManyToOne
    @JoinColumn(name = "DOCUMENTID", referencedColumnName = "DOCUMENTID")
    private DocumentEntity document;

    @Override
    public String toString() {
        return "StudentDocumentsEntity{" +
                "studentDocumentID='" + studentDocumentID + '\'' +
                ", submitted=" + submitted +
                ", submissionDate=" + submissionDate +
                ", verified=" + verified +
                ", students=" + students ;
    }
    //se me cerro el push bro
}
