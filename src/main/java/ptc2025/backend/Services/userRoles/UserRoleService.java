package ptc2025.backend.Services.userRoles;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.userRoles.userRolesEntity;
import ptc2025.backend.Models.DTO.userRoles.userRolesDTO;
import ptc2025.backend.Respositories.userRoles.userRoleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class userRoleService {

    @Autowired
    private userRoleRepository repo;

    public List<userRolesDTO> getUserRoles() {
        List<userRolesEntity> Lista = repo.findAll();
        return Lista.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private userRolesDTO convertirADTO(userRolesEntity userRoles){
        userRolesDTO dto = new userRolesDTO();
        dto.setId(userRoles.getId());
        dto.setUserID(userRoles.getUserID());
        dto.setRoleType(userRoles.getRoleType());
        return dto;
    }
}
