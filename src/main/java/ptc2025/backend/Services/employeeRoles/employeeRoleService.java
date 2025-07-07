package ptc2025.backend.Services.employeeRoles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.employeeRoles.employeeRolesEntity;
import ptc2025.backend.Models.DTO.employeeRoles.employeeRolesDTO;
import ptc2025.backend.Respositories.employeeRoles.employeeRoleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class employeeRoleService {

    @Autowired
    private employeeRoleRepository repo;

    public List<employeeRolesDTO> getAllEmployeeRoles(){
        List<employeeRolesEntity> employiRol = repo.findAll();
        return employiRol.stream()
                .map(this::convertirEmployiRolADTO)
                .collect(Collectors.toList());
    }

    public employeeRolesDTO convertirEmployiRolADTO(employeeRolesEntity emploRol){
        employeeRolesDTO dto = new employeeRolesDTO();
        dto.setId(emploRol.getId());
        dto.setUniversityID(emploRol.getUniversityID());
        dto.setRoleName(emploRol.getRoleName());
        dto.setRoleType(emploRol.getRoleType());
        return dto;
    }
}
