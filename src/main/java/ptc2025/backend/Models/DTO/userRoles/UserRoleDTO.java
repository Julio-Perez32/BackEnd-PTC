package ptc2025.backend.Models.DTO.userRoles;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRoleDTO {

    @NotBlank(message = "El ID del rol no puede estar vacío")
    private String id;

    @NotBlank(message = "El nombre del rol es obligatorio")
    private String name;

    private String description;

    private Integer accessLevel;

    private Boolean isActive = true;

    public UserRoleDTO() { }

    public UserRoleDTO(String id, String name, String description, Integer accessLevel, Boolean isActive) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.accessLevel = accessLevel;
        this.isActive = isActive;
    }
}
