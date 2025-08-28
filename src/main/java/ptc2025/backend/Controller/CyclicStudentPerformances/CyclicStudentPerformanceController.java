package ptc2025.backend.Controller.CyclicStudentPerformances;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Models.DTO.cyclicStudentPerformances.CyclicStudentPerformanceDTO;
import ptc2025.backend.Services.cyclicStudentPerformances.CyclicStudentPerformanceService;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/CyclicStudentPerformance")
@CrossOrigin
public class CyclicStudentPerformanceController {

    @Autowired
    private CyclicStudentPerformanceService service;

    @GetMapping("/getPerformances")
    public List<CyclicStudentPerformanceDTO> getPerformances() {
        return service.getPerformances();
    }

    @GetMapping("/getPerformancesPagination")
    public ResponseEntity<Page<CyclicStudentPerformanceDTO>> getPerformancesPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<CyclicStudentPerformanceDTO> levels = service.getPerformancesPagination(page, size);

        if(levels.isEmpty()){
            return ResponseEntity.badRequest().body(Page.empty());
        }
        return ResponseEntity.ok(levels);
    }

    @PostMapping("/insertPerformance")
    public ResponseEntity<Map<String, Object>> insertPerformance(@Valid @RequestBody CyclicStudentPerformanceDTO dto, BindingResult binding) {
        if(binding.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            binding.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(Map.of("status","error","errors", errors));
        }
        try{
            CyclicStudentPerformanceDTO result = service.insertPerformance(dto);
            if(result == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("status","error","message","Error al insertar rendimiento"));
            }
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("status","success","data", result));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status","server error","message","Error interno del servidor","detail", e.getMessage()));
        }
    }

    @PutMapping("/updatePerformance/{id}")
    public ResponseEntity<?> updatePerformance(@PathVariable String id, @Valid @RequestBody CyclicStudentPerformanceDTO dto, BindingResult binding) {
        if(binding.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            binding.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
        try{
            CyclicStudentPerformanceDTO result = service.updatePerformance(id, dto);
            if(result == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("status","error","message","Error al actualizar rendimiento"));
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(Map.of("status","success","data", result));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status","server error","message","Error interno del servidor","detail", e.getMessage()));
        }
    }

    @DeleteMapping("/deletePerformance/{id}")
    public ResponseEntity<Map<String, Object>> deletePerformance(@PathVariable String id){
        try{
            if(!service.deletePerformance(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error","not found","message","No se encontró el rendimiento","timestamp", Instant.now().toString()));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("status","process completed","message","Rendimiento eliminado con éxito"));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status","server error","message","Error interno del servidor","detail", e.getMessage()));
        }
    }
}
