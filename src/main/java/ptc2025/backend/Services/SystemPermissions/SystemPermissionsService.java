package ptc2025.backend.Services.SystemPermissions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.SystemPermissions.SystemPermissionsEntity;
import ptc2025.backend.Exceptions.ExceptionBadRequest;
import ptc2025.backend.Exceptions.ExceptionNotFound;
import ptc2025.backend.Exceptions.ExceptionServerError;
import ptc2025.backend.Models.DTO.SystemPermissions.SystemPermissionsDTO;
import ptc2025.backend.Respositories.SystemPermissions.SystemPermissionsRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SystemPermissionsService {

    @Autowired
    private SystemPermissionsRepository repo;

    public List<SystemPermissionsDTO> GetsystemPermissions() {
        try {
            List<SystemPermissionsEntity> Lista = repo.findAll();
            return Lista.stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al obtener la lista de permisos del sistema", e);
            throw new ExceptionServerError("Error interno al obtener la lista de permisos");
        }
    }

    private SystemPermissionsDTO convertirADTO(SystemPermissionsEntity systemPermissions){
        SystemPermissionsDTO dto = new SystemPermissionsDTO();
        dto.setId(systemPermissions.getId());
        dto.setCategoryID(systemPermissions.getCategoryID());
        dto.setPermissionName(systemPermissions.getPermissionName());
        dto.setManagePermissions(systemPermissions.isManagePermissions());
        return dto;
    }

    public SystemPermissionsDTO insertarDatos(SystemPermissionsDTO data) {
        if (data == null) {
            throw new ExceptionBadRequest("Los datos proporcionados no pueden ser nulos");
        }
        try {
            SystemPermissionsEntity entity = convertirAEntity(data);
            SystemPermissionsEntity datoGuardado = repo.save(entity);
            return convertirADTO(datoGuardado);
        } catch (Exception e) {
            log.error("Error al registrar el nuevo permiso: " + e.getMessage());
            throw new ExceptionServerError("Error interno al registrar el nuevo permiso");
        }
    }

    private SystemPermissionsEntity convertirAEntity(SystemPermissionsDTO data) {
        SystemPermissionsEntity entity = new SystemPermissionsEntity();
        entity.setCategoryID(data.getCategoryID());
        entity.setPermissionName(data.getPermissionName());
        entity.setManagePermissions(data.isManagePermissions());
        return entity;
    }

    public SystemPermissionsDTO ActualizarDatos(String id, SystemPermissionsDTO json) {
        SystemPermissionsEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionNotFound("Permiso del sistema no encontrado con el ID: " + id));
        try {
            existente.setCategoryID(json.getCategoryID());
            existente.setPermissionName(json.getPermissionName());
            existente.setManagePermissions(json.isManagePermissions());
            SystemPermissionsEntity datoActualizado = repo.save(existente);
            return convertirADTO(datoActualizado);
        } catch (Exception e) {
            log.error("Error al actualizar el permiso con ID " + id, e);
            throw new ExceptionServerError("Error interno al actualizar el permiso");
        }
    }

    public boolean eliminarRegistro(String id) {
        try {
            SystemPermissionsEntity existente = repo.findById(id).orElse(null);
            if (existente != null) {
                repo.deleteById(id);
                return true;
            } else {
                throw new ExceptionNotFound("No se encontró el permiso con ID: " + id + " para eliminar");
            }
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionNotFound("No se encontró el permiso con ID: " + id + " para eliminar");
        } catch (Exception e) {
            log.error("Error interno al eliminar el permiso con ID " + id, e);
            throw new ExceptionServerError("Error interno al eliminar el permiso");
        }
    }
}
