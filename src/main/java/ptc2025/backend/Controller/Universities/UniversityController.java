package ptc2025.backend.Controller.Universities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ptc2025.backend.Models.DTO.Universities.UniversityDTO;
import ptc2025.backend.Services.Universities.UniversityServices;

import java.util.List;


@RestController
@RequestMapping("/apiUniversity") //los endPoint se anteceden de la palabra api
public class UniversityController {
    @Autowired
    private UniversityServices services;

    @GetMapping("/getDataUniversity")
    public List<UniversityDTO> getUniversity() {
        return services.getUniversityService();
    }



}
