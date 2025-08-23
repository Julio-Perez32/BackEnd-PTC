package ptc2025.backend.Controller.CareerSocialServiceProjects;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Models.DTO.careerSocialServiceProjects.CareerSocialServiceProjectDTO;
import ptc2025.backend.Services.careerSocialServiceProjects.CareerSocialServiceProjectService;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/CareerSocialServiceProjects")
public class CareerSocialServiceProjectController {

    @Autowired
    private CareerSocialServiceProjectService service;

    @GetMapping("/getProjects")
    public List<CareerSocialServiceProjectDTO> getProjects() {
        return service.getProjects();
    }

    @PostMapping("/insertProject")
    public ResponseEntity<Map<String, Object>> insertProject(@Valid @RequestBody CareerSocialServiceProjectDTO dto, BindingResult binding){
        if(binding.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            binding.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(Map.of("status","error","errors", errors));
        }
        try{
            CareerSocialServiceProjectDTO result = service.insertProject(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("status","success","data", result));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status","server error","message","Error interno del servidor","detail", e.getMessage()));
        }
    }

    @PutMapping("/updateProject/{id}")
    public ResponseEntity<Map<String,Object>> updateProject(@PathVariable String id, @Valid @RequestBody CareerSocialServiceProjectDTO dto, BindingResult binding){
        if(binding.hasErrors()){
            Map<String, Object> errors = new HashMap<>();
            binding.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
        try{
            CareerSocialServiceProjectDTO result = service.updateProject(id, dto);
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(Map.of("status","success","data", result));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status","server error","message","Error interno del servidor","detail", e.getMessage()));
        }
    }

    @DeleteMapping("/deleteProject/{id}")
    public ResponseEntity<Map<String,Object>> deleteProject(@PathVariable String id){
        try{
            if(!service.deleteProject(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("status","error","message","No se encontró el proyecto","timestamp", Instant.now().toString()));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("status","success","message","Proyecto eliminado con éxito"));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status","server error","message","Error interno del servidor","detail", e.getMessage()));
        }
    }
}
