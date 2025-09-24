package ptc2025.backend.Services.Auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptc2025.backend.Config.Argon2.Argon2Password;
import ptc2025.backend.Entities.Users.UsersEntity;
import ptc2025.backend.Models.DTO.UserProfile.UserProfileDTO;
import ptc2025.backend.Respositories.Users.UsersRespository;

import java.util.Optional;

@Slf4j
@Service
public class AuthService {
    @Autowired
    public UsersRespository repo;


    public boolean Login (String email, String password){
        Argon2Password objHash = new Argon2Password();
        Optional<UsersEntity> List = repo.findByEmail(email).stream().findFirst();
        if(List.isPresent()){

            UsersEntity users = List.get();


            String userType = users.getSystemRoles().getRoleName();
            System.out.println("Usuario con el Id encontrado: " + users.getId()+
                    ", correo: "+ users.getEmail()+
                    ", rol: " + userType);
            return objHash.VerifyPassword(users.getContrasena(), password);
        }

        return false;

    }
    public Optional <UsersEntity> getUser(String email){
        Optional<UsersEntity> userOptional = repo.findByEmail(email);
        return (userOptional != null) ? userOptional : null;
    }

    public Optional<UserProfileDTO> getUserProfile(String email) {
        Optional<UsersEntity> userOpt = repo.findByEmail(email);
        if (userOpt.isPresent()) {
            UsersEntity user = userOpt.get();
            if (user.getPeople() != null) {
                UserProfileDTO dto = new UserProfileDTO(
                        user.getId(),
                        user.getEmail(),
                        user.getSystemRoles().getRoleName(),
                        user.getPeople().getFirstName(),
                        user.getPeople().getLastName(),
                        user.getPeople().getBirthDate(),
                        user.getPeople().getContactEmail(),
                        user.getPeople().getPhone()
                );
                return Optional.of(dto);
            }
        }
        return Optional.empty();
    }


}
