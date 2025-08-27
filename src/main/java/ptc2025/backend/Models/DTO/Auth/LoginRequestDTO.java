package ptc2025.backend.Models.DTO.Auth;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Data
public class LoginRequestDTO {

    private String email;
    private String password;
}
