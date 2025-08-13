package ptc2025.backend.Services;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.UserEntity;
import ptc2025.backend.Models.DTO.UserDTO;
import ptc2025.backend.Respositories.UserRespository;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
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

    public UserDTO insertarDatos(UserDTO data) {
        if (data == null || data.getContrasena() == null || data.getContrasena().isEmpty()){
            throw new IllegalArgumentException("Usuario o contraseña no pueden ser nulos");
        }
        try {
            UserEntity entity = convertirAEntity(data);
            UserEntity usuarioGuardado = repo.save(entity);
            return convertirUsuarioADTO(usuarioGuardado);
        }catch (Exception e){
           log.error("Error al registrar el usuario" + e.getMessage());
           throw new RuntimeException("Error al registrar el usuario");
        }
    }


    private UserEntity convertirAEntity(UserDTO data){
        UserEntity entity = new UserEntity();
        entity.setUniversityID(data.getUniversityID());
        entity.setEmail(data.getEmail());
        entity.setUsuario(data.getUsuario());
        entity.setContrasena(data.getContrasena());
        entity.setFechaCreacion(data.getFechaCreacion());
        return entity;
    }

    public UserDTO actualizarDatos(String id, UserDTO json) {
        UserEntity existente = repo.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        existente.setUniversityID(json.getUniversityID());
        existente.setEmail(json.getEmail());
        existente.setUsuario(json.getUsuario());
        existente.setContrasena(json.getContrasena());
        existente.setFechaCreacion(json.getFechaCreacion());
        UserEntity usuarioActualizado = repo.save(existente);
        return convertirUsuarioADTO(usuarioActualizado);
    }

    public boolean eliminarUsuario(String id) {
        try {
            UserEntity existente = repo.findById(id).orElse(null);
            if (existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro el usuario", 1);
        }
    }
}

