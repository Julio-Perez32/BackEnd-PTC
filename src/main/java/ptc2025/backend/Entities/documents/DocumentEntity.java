package ptc2025.backend.Entities.documents;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "documents")
public class DocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El tipo de documento es oblicatorio")
    private String type;

    @NotBlank(message = "El número es obligatorio")
    @Size(min = 5, max = 20, message = "El número debe tener entre 5 y 20 caracteres")
    private String number;

    @NotNull(message = "La fecha es obligatorio")
    private LocalDate issueDate;

    @NotBlank(message = "El país es obligatorio")
    private String country;

    //Constructor
    public DocumentEntity() {}

    public DocumentEntity(String type, String number, LocalDate issueDate, String country) {
        this.type = type;
        this.number = number;
        this.issueDate = issueDate;
        this.country = country;
    }

    //Getter y Setter


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
