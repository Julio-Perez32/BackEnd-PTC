package ptc2025.backend.Services.systemPermissions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.systemPermissions.systemPermissionsEntity;
import ptc2025.backend.Models.DTO.systemPermissions.systemPermissionsDTO;
import ptc2025.backend.Respositories.systemPermissions.systemPermissionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class systemPermissionService {

    @Autowired
    private systemPermissionRepository repo;

    public List<systemPermissionsDTO> GetsystemPermissions() {
        List<systemPermissionsEntity> Lista = repo.findAll();
        return Lista.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private systemPermissionsDTO convertirADTO(systemPermissionsEntity systemPermissions){
        systemPermissionsDTO dto = new systemPermissionsDTO();
        dto.setId(systemPermissions.getId());
        dto.setCategoryID(systemPermissions.getCategoryID());
        dto.setPermissionName(systemPermissions.getPermissionName());
        dto.setManagePermissions(systemPermissions.isManagePermissions());
        return dto;
    }
}
