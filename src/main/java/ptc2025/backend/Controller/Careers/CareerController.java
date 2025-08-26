package ptc2025.backend.Controller.Careers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Models.DTO.careers.CareerDTO;
import ptc2025.backend.Services.careers.CareerService;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/Careers")
public class CareerController {

    @Autowired
    private CareerService service;

    // GET
    @GetMapping("/getCareers")
    public List<CareerDTO> getCareers() {
        return service.getCareers();
    }

    @GetMapping("/getCareersPaginated")
    public ResponseEntity<Map<String, Object>> getCareersPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            var careersPage = service.getCareersPaginated(page, size);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("data", careersPage.getContent());
            response.put("currentPage", careersPage.getNumber());
            response.put("totalItems", careersPage.getTotalElements());
            response.put("totalPages", careersPage.getTotalPages());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "server error",
                            "message", "Error al obtener carreras paginadas",
                            "detail", e.getMessage()
                    ));
        }
    }


    // POST
    @PostMapping("/insertCareer")
    public ResponseEntity<Map<String, Object>> insertCareer(@Valid @RequestBody CareerDTO dto, BindingResult binding) {
        if (binding.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            binding.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("status", "error", "errors", errors));
        }
        try {
            CareerDTO answer = service.insertCareer(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("status", "success", "data", answer));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "server error", "message", "Error interno del servidor", "detail", e.getMessage()));
        }
    }

    // PUT
    @PutMapping("/updateCareer/{id}")
    public ResponseEntity<Map<String, Object>> updateCareer(@PathVariable String id, @Valid @RequestBody CareerDTO dto, BindingResult binding) {
        if (binding.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            binding.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("status", "error", "errors", errors));
        }
        try {
            CareerDTO answer = service.updateCareer(id, dto);
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(Map.of("status", "success", "data", answer));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "server error", "message", "Error interno del servidor", "detail", e.getMessage()));
        }
    }

    // DELETE
    @DeleteMapping("/deleteCareer/{id}")
    public ResponseEntity<Map<String, Object>> deleteCareer(@PathVariable String id) {
        try {
            if (!service.deleteCareer(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "status", "error",
                                "message", "No se encontró la carrera",
                                "timestamp", Instant.now().toString()
                        ));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("status", "success", "message", "Carrera eliminada con éxito"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "server error", "message", "Error interno del servidor", "detail", e.getMessage()));
        }
    }
}
