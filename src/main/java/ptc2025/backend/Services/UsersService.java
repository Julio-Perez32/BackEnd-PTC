package ptc2025.backend.Services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.UsersEntity;
import ptc2025.backend.Models.DTO.UsersDTO;
import ptc2025.backend.Respositories.UsersRespository;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class UsersService {

    @Autowired
    private UsersRespository repo;

    public List<UsersDTO> getAllUsers(){
        List<UsersEntity> users = repo.findAll();
        return users.stream()
                .map(this::convertirUsuarioADTO)
                .collect(Collectors.toList());
    }

    public UsersDTO convertirUsuarioADTO(UsersEntity usuario){
        UsersDTO dto = new UsersDTO();
        dto.setId(usuario.getId());
        dto.setUniversityID(usuario.getUniversityID());
        dto.setEmail(usuario.getEmail());
        dto.setUsuario(usuario.getUsuario());
        dto.setContrasena(usuario.getContrasena());
        dto.setFechaCreacion(usuario.getFechaCreacion());
        return dto;
    }

    public UsersDTO insertarDatos(UsersDTO data) {
        if (data == null || data.getContrasena() == null || data.getContrasena().isEmpty()){
            throw new IllegalArgumentException("Usuario o contraseña no pueden ser nulos");
        }
        try {
            UsersEntity entity = convertirAEntity(data);
            UsersEntity usuarioGuardado = repo.save(entity);
            return convertirUsuarioADTO(usuarioGuardado);
        }catch (Exception e){
           log.error("Error al registrar el usuario" + e.getMessage());
           throw new RuntimeException("Error al registrar el usuario");
        }
    }


    private UsersEntity convertirAEntity(UsersDTO data){
        UsersEntity entity = new UsersEntity();
        entity.setUniversityID(data.getUniversityID());
        entity.setEmail(data.getEmail());
        entity.setUsuario(data.getUsuario());
        entity.setContrasena(data.getContrasena());
        entity.setFechaCreacion(data.getFechaCreacion());
        return entity;
    }

    public UsersDTO actualizarDatos(String id, UsersDTO json) {
        UsersEntity existente = repo.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        existente.setUniversityID(json.getUniversityID());
        existente.setEmail(json.getEmail());
        existente.setUsuario(json.getUsuario());
        existente.setContrasena(json.getContrasena());
        existente.setFechaCreacion(json.getFechaCreacion());
        UsersEntity usuarioActualizado = repo.save(existente);
        return convertirUsuarioADTO(usuarioActualizado);
    }

    public boolean eliminarUsuario(String id) {
        try {
            UsersEntity existente = repo.findById(id).orElse(null);
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

