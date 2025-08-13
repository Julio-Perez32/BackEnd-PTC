package ptc2025.backend.Controller.systemPermissions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ptc2025.backend.Models.DTO.systemPermissions.systemPermissionsDTO;
import ptc2025.backend.Services.systemPermissions.systemPermissionService;

import java.util.List;

@RestController
@RequestMapping("/systemPermissions")
public class systemPermissionsController {

    @Autowired
    private systemPermissionService service;

    @GetMapping("/getSystemPermissions")
    List<systemPermissionsDTO> getData(){ return service.GetsystemPermissions();}
}
