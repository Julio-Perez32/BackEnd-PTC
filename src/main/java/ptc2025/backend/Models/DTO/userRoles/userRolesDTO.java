package ptc2025.backend.Models.DTO.userRoles;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class userRolesDTO {
    private String id;
    @NotBlank
    private String userID;
    @NotBlank
    private String roleType;
}
