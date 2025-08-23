package ptc2025.backend.Services.SystemPermissions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.SystemPermissions.SystemPermissionsEntity;
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
        List<SystemPermissionsEntity> Lista = repo.findAll();
        return Lista.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private SystemPermissionsDTO convertirADTO(SystemPermissionsEntity systemPermissions){
        SystemPermissionsDTO dto = new SystemPermissionsDTO();
        dto.setId(systemPermissions.getId());
        dto.setCategoryID(systemPermissions.getCategoryID());
        dto.setPermissionName(systemPermissions.getPermissionName());
        dto.setManagePermissions(systemPermissions.isManagePermissions());
        return dto;
    }

    public SystemPermissionsDTO insertarDatos( SystemPermissionsDTO data) {
        if (data == null){
            throw new IllegalArgumentException("Datos invalidos");
        }
        try {
            SystemPermissionsEntity entity = convertirAEntity(data);
            SystemPermissionsEntity datoGuardado = repo.save(entity);
            return convertirADTO(datoGuardado);
        }catch (Exception e){
            log.error("Error al registrar el nuevo dato " + e.getMessage());
            throw new IllegalArgumentException("Error al ingresar el dato");
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
        SystemPermissionsEntity existente = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Dato no encontrado"));
        existente.setCategoryID(json.getCategoryID());
        existente.setPermissionName(json.getPermissionName());
        existente.setManagePermissions(json.isManagePermissions());
        SystemPermissionsEntity datoActualizado = repo.save(existente);
        return convertirADTO(datoActualizado);
    }

    public boolean eliminarRegistro(String id) {
        try {
            SystemPermissionsEntity existente = repo.findById(id).orElse(null);
            if (existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("no se encontro el registro que se deseaba eliminar", 1);
        }
    }
}
