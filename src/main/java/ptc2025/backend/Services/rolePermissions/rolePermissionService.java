package ptc2025.backend.Services.rolePermissions;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.rolePermissions.rolePermissionsEntity;
import ptc2025.backend.Models.DTO.rolePermissions.rolePermissionsDTO;
import ptc2025.backend.Respositories.rolePermissions.rolePermissionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class rolePermissionService {
    @Autowired
    private rolePermissionRepository repo;

    public List<rolePermissionsDTO> getRolePermissions() {
        List<rolePermissionsEntity> Lista = repo.findAll();
        return Lista.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private rolePermissionsDTO convertirADTO(rolePermissionsEntity rolePermissions){
        rolePermissionsDTO dto = new rolePermissionsDTO();
        dto.setId(rolePermissions.getId());
        dto.setPermissionID(rolePermissions.getPermissionID());
        dto.setRoleID(rolePermissions.getRoleID());
        return dto;
    }

    public rolePermissionsDTO insertarDatos(rolePermissionsDTO data) {
        if (data == null){
            throw new RuntimeException("Datos no validos");
        }
        try {
            rolePermissionsEntity entity = convertirAEntity(data);
            rolePermissionsEntity rolePermissionsGuardado = repo.save(entity);
            return convertirADTO(rolePermissionsGuardado);
        }catch (Exception e){
            log.error("error al registrar el nuevo dato" + e.getMessage());
            throw new RuntimeException("Error al registrar el nuevo dato");
        }
    }

    private rolePermissionsEntity convertirAEntity(rolePermissionsDTO data){
        rolePermissionsEntity entity = new rolePermissionsEntity();
        entity.setPermissionID(data.getPermissionID());
        entity.setRoleID(data.getRoleID());
        return entity;
    }

    public rolePermissionsDTO actualizarDatos(String id, rolePermissionsDTO json) {
        rolePermissionsEntity existente = repo.findById(id).orElseThrow(() -> new RuntimeException("Registro no encontrado"));
        existente.setPermissionID(json.getPermissionID());
        existente.setRoleID(json.getRoleID());
        rolePermissionsEntity datosActualizados = repo.save(existente);
        return convertirADTO(datosActualizados);
    }

    public boolean eliminarDatos(String id) {
        try {
            rolePermissionsEntity existente = repo.findById(id).orElse(null);
            if (existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro el registro", 1);
        }
    }
}
