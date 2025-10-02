package ptc2025.backend.Models.DTO.UserProfile;

import lombok.*;
import ptc2025.backend.Entities.systemRoles.SystemRolesEntity;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
    //datos users
    private String userID;
    private String email;
    private String roleID;

    //datos people
    private String firstName;
    private String lastName;
    private Date birthdate;
    private String contactEmail;
    private String phone;

    //datos university
    private String universityID;

}
