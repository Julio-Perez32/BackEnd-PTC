package ptc2025.backend.Controller.Modalities;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ptc2025.backend.Models.DTO.Modalities.ModalitiesDTO;
import ptc2025.backend.Services.Modalities.ModalityService;

import java.util.List;

@RestController
@RequestMapping("/Modalities")
public class ModalitiesController {
    @Autowired
    private ModalityService service;

    @GetMapping("/getModalities")
    public List<ModalitiesDTO> getData() { return service.getAllModalities(); }
}
