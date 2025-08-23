package ptc2025.backend.Services.RolePermissions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.RolePermissions.RolePermissionsEntity;
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
        List<RolePermissionsEntity> Lista = repo.findAll();
        return Lista.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private RolePermissionsDTO convertirADTO(RolePermissionsEntity rolePermissions){
        RolePermissionsDTO dto = new RolePermissionsDTO();
        dto.setId(rolePermissions.getId());
        dto.setPermissionID(rolePermissions.getPermissionID());
        dto.setRoleID(rolePermissions.getRoleID());
        return dto;
    }

    public RolePermissionsDTO insertarDatos(RolePermissionsDTO data) {
        if (data == null){
            throw new RuntimeException("Datos no validos");
        }
        try {
            RolePermissionsEntity entity = convertirAEntity(data);
            RolePermissionsEntity rolePermissionsGuardado = repo.save(entity);
            return convertirADTO(rolePermissionsGuardado);
        }catch (Exception e){
            log.error("error al registrar el nuevo dato" + e.getMessage());
            throw new RuntimeException("Error al registrar el nuevo dato");
        }
    }

    private RolePermissionsEntity convertirAEntity(RolePermissionsDTO data){
        RolePermissionsEntity entity = new RolePermissionsEntity();
        entity.setPermissionID(data.getPermissionID());
        entity.setRoleID(data.getRoleID());
        return entity;
    }

    public RolePermissionsDTO actualizarDatos(String id, RolePermissionsDTO json) {
        RolePermissionsEntity existente = repo.findById(id).orElseThrow(() -> new RuntimeException("Registro no encontrado"));
        existente.setPermissionID(json.getPermissionID());
        existente.setRoleID(json.getRoleID());
        RolePermissionsEntity datosActualizados = repo.save(existente);
        return convertirADTO(datosActualizados);
    }

    public boolean eliminarDatos(String id) {
        try {
            RolePermissionsEntity existente = repo.findById(id).orElse(null);
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
