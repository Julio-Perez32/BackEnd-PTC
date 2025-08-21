package ptc2025.backend.Controller.courseEnrollments;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Models.DTO.courseEnrollments.CourseEnrollmentDTO;
import ptc2025.backend.Services.courseEnrollments.CourseEnrollmentService;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apiCourseEnrollments")
public class CourseEnrollmentController {

    @Autowired
    private CourseEnrollmentService service;

    @GetMapping("/getEnrollments")
    public List<CourseEnrollmentDTO> getEnrollments() {
        return service.getEnrollments();
    }

    @PostMapping("/insertEnrollment")
    public ResponseEntity<Map<String, Object>> insertEnrollment(@Valid @RequestBody CourseEnrollmentDTO dto, HttpServletRequest request) {
        try {
            CourseEnrollmentDTO answer = service.insertEnrollment(dto);
            if(answer == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of(
                                "status", "error",
                                "error type", "VALIDATION_ERROR",
                                "message", "Error al insertar inscripción"
                        ));
            }
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "status", "success",
                            "data", answer
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "server error",
                            "message", "Error interno del servidor",
                            "detail", e.getMessage()
                    ));
        }
    }

    @PutMapping("/updateEnrollment/{id}")
    public ResponseEntity<?> updateEnrollment(@PathVariable String id, @Valid @RequestBody CourseEnrollmentDTO dto, BindingResult binding) {
        if(binding.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            binding.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            CourseEnrollmentDTO answer = service.updateEnrollment(id, dto);
            if(answer == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of(
                                "status", "error",
                                "error type", "VALIDATION_ERROR",
                                "message", "Error al actualizar inscripción"
                        ));
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(Map.of(
                            "status", "success",
                            "data", answer
                    ));
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "server error",
                            "message", "Error interno del servidor",
                            "detail", e.getMessage()
                    ));
        }
    }

    @DeleteMapping("/deleteEnrollment/{id}")
    public ResponseEntity<Map<String, Object>> deleteEnrollment(@PathVariable String id) {
        try {
            if(!service.deleteEnrollment(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "error", "not found",
                                "message", "No se encontró la inscripción",
                                "timestamp", Instant.now().toString()
                        ));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of(
                            "status", "process completed",
                            "message", "Inscripción eliminada con éxito"
                    ));
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "server error",
                            "message", "Error interno del servidor",
                            "detail", e.getMessage()
                    ));
        }
    }
}
