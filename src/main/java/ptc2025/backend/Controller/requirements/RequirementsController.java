package ptc2025.backend.Controller.requirements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ptc2025.backend.Models.DTO.Requirements.RequirementsDTO;
import ptc2025.backend.Services.Requirements.RequirementService;

import java.util.List;

@RestController
@RequestMapping("/Requirements")
public class RequirementsController {

    @Autowired
    private RequirementService service;

    @GetMapping("/RequirementsgetAlldata")
    public List<RequirementsDTO> getData() {return service.getAllRequirements(); }
}
