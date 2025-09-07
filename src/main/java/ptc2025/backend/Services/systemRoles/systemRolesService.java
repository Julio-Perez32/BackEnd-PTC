package ptc2025.backend.Services.systemRoles;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.systemRoles.SystemRolesEntity;
import ptc2025.backend.Exceptions.ExceptionBadRequest;
import ptc2025.backend.Exceptions.ExceptionNotFound;
import ptc2025.backend.Exceptions.ExceptionServerError;
import ptc2025.backend.Models.DTO.systemRoles.systemRolesDTO;
import ptc2025.backend.Respositories.systemRoles.systemRolesRespository;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class systemRolesService {
    @Autowired
    systemRolesRespository repo;
    public List<systemRolesDTO> getSystemRoles() {
        try {
            List<SystemRolesEntity> universidad = repo.findAll();
            return universidad.stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al obtener los roles del sistema", e);
            throw new ExceptionServerError("Error interno al obtener los roles del sistema");
        }
    }
    // Insertar nuevo rol
    public systemRolesDTO insertSystemRoles(systemRolesDTO dto) {
        if (dto.getRoleName() == null || dto.getRoleName().isBlank()) {
            throw new ExceptionBadRequest("Todos los campos obligatorios deben estar completos: nombre del rol.");
        }
        try {
            SystemRolesEntity entidad = convertirAEntity(dto);
            SystemRolesEntity guardado = repo.save(entidad);
            return convertirADTO(guardado);
        } catch (Exception e) {
            log.error("Error al registrar un nuevo rol en el sistema: " + e.getMessage(), e);
            throw new ExceptionServerError("Error interno al guardar rol del sistema");
        }
    }

    // Actualizar rol
    public systemRolesDTO updateSystemRoles(String id, systemRolesDTO dto) {
        SystemRolesEntity rolExistente = repo.findById(id)
                .orElseThrow(() -> new ExceptionNotFound("El dato no pudo ser actualizado. Rol del sistema no encontrado"));

        try {
            rolExistente.setRoleName(dto.getRoleName());
            SystemRolesEntity actualizado = repo.save(rolExistente);
            return convertirADTO(actualizado);
        } catch (Exception e) {
            log.error("Error al actualizar el rol con ID " + id, e);
            throw new ExceptionServerError("Error interno al actualizar el rol");
        }
    }

    // Eliminar rol
    public boolean deleteSystemRoles(String id) {
        try {
            SystemRolesEntity objRol = repo.findById(id).orElse(null);
            if (objRol != null) {
                repo.deleteById(id);
                return true;
            } else {
                throw new ExceptionNotFound("Rol del sistema no encontrado con el ID: " + id);
            }
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionNotFound("No se encontró ningún rol con el ID: " + id + " para ser eliminado");
        } catch (Exception e) {
            log.error("Error interno al eliminar el rol con ID " + id, e);
            throw new ExceptionServerError("Error interno al eliminar el rol");
        }
    }

    // Conversión Entity → DTO
    private systemRolesDTO convertirADTO(SystemRolesEntity roles) {
        systemRolesDTO dto = new systemRolesDTO();
        dto.setRoleID(roles.getRoleId());
        dto.setRoleName(roles.getRoleName());
        return dto;
    }

    // Conversión DTO → Entity
    private SystemRolesEntity convertirAEntity(systemRolesDTO dto) {
        SystemRolesEntity entity = new SystemRolesEntity();
        entity.setRoleName(dto.getRoleName());
        return entity;
    }
}
