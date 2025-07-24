package ptc2025.backend.Entities.careers;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "careers")
public class CareerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(unique = true)
    private String name;

    @Size(max = 255, message = "La descripción no puede tener más de 255 caracteres")
    private String description;

    //Constructorse

    public CareerEntity() {}

    public CareerEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    //Getter y Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "El nombre es obligatorio") @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "El nombre es obligatorio") @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres") String name) {
        this.name = name;
    }

    public @Size(max = 255, message = "La descripción no puede tener más de 255 caracteres") String getDescription() {
        return description;
    }

    public void setDescription(@Size(max = 255, message = "La descripción no puede tener más de 255 caracteres") String description) {
        this.description = description;
    }
}
