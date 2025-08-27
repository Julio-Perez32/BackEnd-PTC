package ptc2025.backend.Models.DTO.Auth;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private boolean success;
    private String message;

    private String userId;
    private String email;
    private String role;
    private String personName;

}
