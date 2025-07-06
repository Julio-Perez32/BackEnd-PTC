package ptc2025.backend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.UserEntity;
import ptc2025.backend.Models.DTO.UserDTO;
import ptc2025.backend.Respositories.UserRespository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRespository repo;

    public List<UserDTO> getAllUsers(){
        List<UserEntity> users = repo.findAll();
        return users.stream()
                .map(this::convertirUsuarioADTO)
                .collect(Collectors.toList());
    }

    public UserDTO convertirUsuarioADTO(UserEntity usuario){
        UserDTO dto = new UserDTO();
        dto.setId(usuario.getId());
        dto.setUniversityID(usuario.getUniversityID());
        dto.setEmail(usuario.getEmail());
        dto.setUsuario(usuario.getUsuario());
        dto.setContrasena(usuario.getContrasena());
        dto.setFechaCreacion(usuario.getFechaCreacion());
        return dto;
    }
}

