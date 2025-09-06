package ptc2025.backend.Services.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptc2025.backend.Config.Argon2.Argon2Password;
import ptc2025.backend.Entities.Users.UsersEntity;
import ptc2025.backend.Respositories.Users.UsersRespository;

import java.util.Optional;

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

}
