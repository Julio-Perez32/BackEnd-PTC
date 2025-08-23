package ptc2025.backend.Services.UserRoles;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.UserRoles.UserRolesEntity;
import ptc2025.backend.Models.DTO.UserRoles.UserRolesDTO;
import ptc2025.backend.Respositories.UserRoles.UserRolesRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserRolesService {
    @Autowired
    UserRolesRepository repo;

    public List<UserRolesDTO> getUserRole(){
        List<UserRolesEntity> universidad = repo.findAll();
        return universidad.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public UserRolesDTO insertUserRole (UserRolesDTO dto){
        if (dto.getUserid() == null || dto.getUserid().isBlank() || dto.getRoleType() == null || dto.getRoleType().isBlank()) {
            throw new IllegalArgumentException("Todos los campos obligatorios deben estar completos: nombre del usuario y tipo de rol");
        }
        try {
            // Convertir DTO → Entity
            UserRolesEntity entidad = convertirAEntity(dto);

            // Guardar en BD
            UserRolesEntity guardado = repo.save(entidad);

            // Convertir Entity → DTO
            return convertirADTO(guardado);
        }catch (Exception e){
            log.error("Error al registrar el rol de usuario " + e.getMessage());
            throw new RuntimeException("Error interno al guardar el rol");
        }
    }
    //Put
    public UserRolesDTO updateUserRole (String id, UserRolesDTO dto){
        UserRolesEntity existente = new UserRolesEntity();
        existente.setUserid(dto.getUserid());
        existente.setRoleType(dto.getRoleType());
        UserRolesEntity actualizar = repo.save(existente);
        return convertirADTO(actualizar);
    }
    //Delete
    public boolean deleteUserRole(String id){
        try {
            UserRolesEntity objCompo = repo.findById(id).orElse(null);
            if (objCompo != null){
                repo.deleteById(id);
                return true;
            }else{
                System.out.println("Rol de Usuario no encontrado");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontraron ningun rol de usuario con el id: ", 1);

        }
    }

    public UserRolesDTO convertirADTO (UserRolesEntity entity){
        UserRolesDTO dto = new UserRolesDTO();
        dto.setUserRoleid(entity.getUserRoleid());
        dto.setUserid(entity.getUserid());
        dto.setRoleType(entity.getRoleType());
        return dto;
    }
    public UserRolesEntity convertirAEntity (UserRolesDTO dto){
        UserRolesEntity entity = new UserRolesEntity();
        entity.setUserid(dto.getUserid());
        entity.setUserRoleid(dto.getUserRoleid());
        entity.setRoleType(dto.getRoleType());
        return entity;
    }


}
