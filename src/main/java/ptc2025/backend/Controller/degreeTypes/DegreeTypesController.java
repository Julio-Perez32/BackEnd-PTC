package ptc2025.backend.Controller.degreeTypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ptc2025.backend.Models.DTO.degreeTypes.DegreeTypesDTO;
import ptc2025.backend.Services.degreeTypes.DegreeTypeService;

import java.util.List;

@RestController
@RequestMapping("/DegreeTypes")
public class DegreeTypesController {
    @Autowired
    private DegreeTypeService service;

    @GetMapping("/getDegreeTypes")
    public List<DegreeTypesDTO> getData() { return service.getAllDegreeTypes(); }
}
