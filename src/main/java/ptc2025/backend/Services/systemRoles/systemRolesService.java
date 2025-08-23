package ptc2025.backend.Services.systemRoles;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.systemRoles.SystemRolesEntity;
import ptc2025.backend.Models.DTO.systemRoles.systemRolesDTO;
import ptc2025.backend.Respositories.systemRoles.systemRolesRespository;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class systemRolesService {
    @Autowired
    systemRolesRespository repo;
    public List<systemRolesDTO> getSystemRoles(){
        List<SystemRolesEntity> universidad = repo.findAll();
        return universidad.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    public systemRolesDTO insertSystemRoles(systemRolesDTO dto){
        // Validaciones combinadas
        if (dto.getRoleName() == null || dto.getRoleName().isBlank()) {
            throw new IllegalArgumentException("Todos los campos obligatorios deben estar completos: nombre del rol.");
        }
        try {
            // Convertir DTO → Entity
            SystemRolesEntity entidad = convertirAEntity(dto);

            // Guardar en BD
            SystemRolesEntity guardado = repo.save(entidad);

            // Convertir Entity → DTO
            return convertirADTO(guardado);
        }catch (Exception e){
            log.error("Error al registrar un nuevo rol en el systema " + e.getMessage());
            throw new RuntimeException("Error interno al guardar rol del sistema");
        }

    }
    public systemRolesDTO updateSystemRoles(String id, systemRolesDTO dto){
        SystemRolesEntity rolExistente = repo.findById(id).orElseThrow(() -> new RuntimeException("El dato no pudo ser actualizado. Rol del sistema no encontrado no encontrada"));
        //Actualizacion de los datos
        rolExistente.setRoleName(dto.getRoleName());

        SystemRolesEntity actulizado = repo.save(rolExistente);
        return convertirADTO(actulizado);
    }
    public boolean deleteSystemRoles (String id){
        try {
            //Validacion de existencia de Universidad
            SystemRolesEntity objRol = repo.findById(id).orElse(null);
            //Si existe se procede a eliminar
            if (objRol != null){
                repo.deleteById(id);
                return true;
            }else {
                System.out.println("Rol del sistema no encontrado");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro ninguna Rol con el ID:" + id + "para poder ser eliminada", 1);
        }
    }
    private systemRolesDTO convertirADTO(SystemRolesEntity roles){
        systemRolesDTO dto = new systemRolesDTO();
        dto.setRoleID(roles.getRoleId());
        dto.setRoleName(roles.getRoleName());
        return dto;
    }
    private SystemRolesEntity convertirAEntity(systemRolesDTO dto){
        SystemRolesEntity entity = new SystemRolesEntity();
        entity.setRoleName(dto.getRoleName());
        return entity;
    }
}
