package ptc2025.backend.Entities.employees;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "employees")
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    private String lastName;

    @NotNull(message = "El cargo es obligatorio")
    @Enumerated(EnumType.STRING)
    private RoleType position;

    @Email(message = "El correo debe de ser existente")
    @NotBlank(message = "El correo es obligatorio")
    private String email;

    public EmployeeEntity() {}

    public EmployeeEntity(String firstName, String lastName, RoleType position, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.email = email;
    }

    //Getter y Setter


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "El nombre es obligatorio") String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotBlank(message = "El nombre es obligatorio") String firstName) {
        this.firstName = firstName;
    }

    public @NotBlank(message = "El apellido es obligatorio") String getLastName() {
        return lastName;
    }

    public void setLastName(@NotBlank(message = "El apellido es obligatorio") String lastName) {
        this.lastName = lastName;
    }

    public @NotNull(message = "El cargo es obligatorio") RoleType getPosition() {
        return position;
    }

    public void setPosition(@NotNull(message = "El cargo es obligatorio") RoleType position) {
        this.position = position;
    }

    public @Email(message = "El correo debe de ser existente") @NotBlank(message = "El correo es obligatorio") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "El correo debe de ser existente") @NotBlank(message = "El correo es obligatorio") String email) {
        this.email = email;
    }
}
