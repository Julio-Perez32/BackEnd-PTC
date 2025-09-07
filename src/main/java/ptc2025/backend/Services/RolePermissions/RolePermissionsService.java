package ptc2025.backend.Services.RolePermissions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.RolePermissions.RolePermissionsEntity;
import ptc2025.backend.Exceptions.ExceptionNoSuchElement;
import ptc2025.backend.Models.DTO.RolePermissions.RolePermissionsDTO;
import ptc2025.backend.Respositories.RolePermissions.RolePermissionsRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RolePermissionsService {

    @Autowired
    private RolePermissionsRepository repo;

    public List<RolePermissionsDTO> getRolePermissions() {
        List<RolePermissionsEntity> lista = repo.findAll();
        return lista.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private RolePermissionsDTO convertirADTO(RolePermissionsEntity rolePermissions) {
        RolePermissionsDTO dto = new RolePermissionsDTO();
        dto.setId(rolePermissions.getId());
        dto.setPermissionID(rolePermissions.getPermissionID());
        dto.setRoleID(rolePermissions.getRoleID());
        return dto;
    }

    public RolePermissionsDTO insertarDatos(RolePermissionsDTO data) {
        if (data == null) {
            throw new IllegalArgumentException("Los datos proporcionados no son válidos");
        }
        try {
            RolePermissionsEntity entity = convertirAEntity(data);
            RolePermissionsEntity rolePermissionsGuardado = repo.save(entity);
            return convertirADTO(rolePermissionsGuardado);
        } catch (IllegalArgumentException e) {
            log.error("Error de validación al registrar el nuevo dato: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al registrar el nuevo dato: {}", e.getMessage());
            throw new RuntimeException("Error al registrar el nuevo dato", e);
        }
    }

    private RolePermissionsEntity convertirAEntity(RolePermissionsDTO data) {
        RolePermissionsEntity entity = new RolePermissionsEntity();
        entity.setPermissionID(data.getPermissionID());
        entity.setRoleID(data.getRoleID());
        return entity;
    }

    public RolePermissionsDTO actualizarDatos(String id, RolePermissionsDTO json) {
        RolePermissionsEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionNoSuchElement("Registro no encontrado con ID: " + id));

        existente.setPermissionID(json.getPermissionID());
        existente.setRoleID(json.getRoleID());

        try {
            RolePermissionsEntity datosActualizados = repo.save(existente);
            return convertirADTO(datosActualizados);
        } catch (Exception e) {
            log.error("Error al actualizar el RolePermission con ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al actualizar el registro", e);
        }
    }

    public boolean eliminarDatos(String id) {
        try {
            RolePermissionsEntity existente = repo.findById(id)
                    .orElseThrow(() -> new ExceptionNoSuchElement("Registro no encontrado con ID: " + id));
            repo.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            log.error("Error al eliminar RolePermission con ID {}: {}", id, e.getMessage());
            throw new EmptyResultDataAccessException("No se encontró el registro para eliminar", 1);
        }
    }
}
