package ptc2025.backend.Controller.employeeRoles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ptc2025.backend.Models.DTO.employeeRoles.employeeRolesDTO;
import ptc2025.backend.Services.employeeRoles.employeeRoleService;

import java.util.List;

@RestController
@RequestMapping("/employeeRoles")
public class employeeRolesController {

    @Autowired
    private employeeRoleService services;

    @GetMapping("/getAllServices")
    public List<employeeRolesDTO> getData() { return services.getAllEmployeeRoles(); }
}
