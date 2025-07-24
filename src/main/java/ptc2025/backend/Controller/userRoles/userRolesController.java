package ptc2025.backend.Controller.userRoles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ptc2025.backend.Models.DTO.userRoles.userRolesDTO;
import ptc2025.backend.Services.userRoles.userRoleService;

import java.util.List;

@RestController
@RequestMapping("/userRoles")
public class userRolesController {

    @Autowired
    private userRoleService service;

    @GetMapping("/getUserRoles")
    public List<userRolesDTO> getData(){return service.getUserRoles();}
}
