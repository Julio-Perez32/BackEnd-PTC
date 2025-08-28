package ptc2025.backend.Controller.CareerCycleAvailability;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Models.DTO.careerCycleAvailability.CareerCycleAvailabilityDTO;
import ptc2025.backend.Services.careerCycleAvailability.CareerCycleAvailabilityService;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/CareerCycleAvailability")
@CrossOrigin
public class CareerCycleAvailabilityController {

    @Autowired
    private CareerCycleAvailabilityService service;

    @GetMapping("/getAvailability")
    public List<CareerCycleAvailabilityDTO> getAvailability() {
        return service.getAvailability();
    }

    @GetMapping("/getAvailabilityPagination")
    public ResponseEntity<Page<CareerCycleAvailabilityDTO>> getAvailabilityPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<CareerCycleAvailabilityDTO> levels = service.getAvailabilityPagination(page, size);

        if(levels.isEmpty()){
            return ResponseEntity.badRequest().body(Page.empty());
        }
        return ResponseEntity.ok(levels);
    }

    @PostMapping("/insertAvailability")
    public ResponseEntity<Map<String, Object>> insertAvailability(@Valid @RequestBody CareerCycleAvailabilityDTO dto, BindingResult binding) {
        if(binding.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            binding.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(Map.of("status","error","errors", errors));
        }
        try{
            CareerCycleAvailabilityDTO result = service.insertAvailability(dto);
            if(result == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("status","error","message","Error al insertar disponibilidad"));
            }
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("status","success","data", result));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status","server error","message","Error interno del servidor","detail", e.getMessage()));
        }
    }

    @PutMapping("/updateAvailability/{id}")
    public ResponseEntity<?> updateAvailability(@PathVariable String id, @Valid @RequestBody CareerCycleAvailabilityDTO dto, BindingResult binding) {
        if(binding.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            binding.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
        try{
            CareerCycleAvailabilityDTO result = service.updateAvailability(id, dto);
            if(result == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("status","error","message","Error al actualizar disponibilidad"));
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(Map.of("status","success","data", result));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status","server error","message","Error interno del servidor","detail", e.getMessage()));
        }
    }

    @DeleteMapping("/deleteAvailability/{id}")
    public ResponseEntity<Map<String, Object>> deleteAvailability(@PathVariable String id){
        try{
            if(!service.deleteAvailability(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error","not found","message","No se encontró la disponibilidad","timestamp", Instant.now().toString()));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("status","process completed","message","Disponibilidad eliminada con éxito"));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status","server error","message","Error interno del servidor","detail", e.getMessage()));
        }
    }
}
