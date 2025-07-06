package ptc2025.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ptc2025.backend.Models.DTO.UserDTO;
import ptc2025.backend.Services.UserService;

import java.util.List;

@RestController
@RequestMapping("/Usuarios")
public class UserController {

    @Autowired
    private UserService services;

    @GetMapping("/getUsers")
    public List<UserDTO> getdata() {return services.getAllUsers(); }

}
