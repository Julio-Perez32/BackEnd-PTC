package ptc2025.backend.Controller.Employees;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Models.DTO.employees.EmployeeDTO;
import ptc2025.backend.Services.employees.EmployeeService;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/Employees")

@CrossOrigin
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    // GET
    @GetMapping("/getEmployees")
    public List<EmployeeDTO> getEmployees() {
        return service.getEmployees();
    }

    // POST
    @PostMapping("/insertEmployee")
    public ResponseEntity<Map<String, Object>> insertEmployee(@Valid @RequestBody EmployeeDTO dto, BindingResult binding) {
        if (binding.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            binding.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status", "error", "errors", errors));
        }
        try {
            EmployeeDTO answer = service.insertEmployee(dto);
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
    @PutMapping("/updateEmployee/{id}")
    public ResponseEntity<Map<String, Object>> updateEmployee(@PathVariable String id, @Valid @RequestBody EmployeeDTO dto, BindingResult binding) {
        if (binding.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            binding.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status", "error", "errors", errors));
        }
        try {
            EmployeeDTO answer = service.updateEmployee(id, dto);
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
    @DeleteMapping("/deleteEmployee/{id}")
    public ResponseEntity<Map<String, Object>> deleteEmployee(@PathVariable String id) {
        try {
            if (!service.deleteEmployee(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "status", "error",
                                "message", "No se encontró el empleado",
                                "timestamp", Instant.now().toString()
                        ));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("status", "success", "message", "Empleado eliminado con éxito"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "server error", "message", "Error interno del servidor", "detail", e.getMessage()));
        }
    }
}
