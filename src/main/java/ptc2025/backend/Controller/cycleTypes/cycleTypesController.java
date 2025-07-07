package ptc2025.backend.Controller.cycleTypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ptc2025.backend.Models.DTO.cycleTypes.cycleTypesDTO;
import ptc2025.backend.Services.cycleTypes.cycleTypeService;

import java.util.List;

@RestController
@RequestMapping("/cycleTypes")
public class cycleTypesController {

    @Autowired
    private cycleTypeService service;

    @GetMapping("/getDataCycleTypes")
    public List<cycleTypesDTO> getdata()
    {
        return service.getAllCycleTypes();
    }
}
