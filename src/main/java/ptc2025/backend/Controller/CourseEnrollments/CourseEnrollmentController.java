package ptc2025.backend.Controller.CourseEnrollments;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Models.DTO.courseEnrollments.CourseEnrollmentDTO;
import ptc2025.backend.Services.courseEnrollments.CourseEnrollmentService;
import org.springframework.data.domain.Page;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/CourseEnrollments")
@CrossOrigin
public class CourseEnrollmentController {

    @Autowired
    private CourseEnrollmentService service;

    // GET
    @GetMapping("/getCourseEnrollments")
    public List<CourseEnrollmentDTO> getEnrollments() {
        return service.getEnrollments();
    }

    @GetMapping("/getEnrollmentsPaginated")
    public ResponseEntity<Map<String, Object>> getEnrollmentsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<CourseEnrollmentDTO> enrollmentsPage = service.getPaginatedEnrollments(page, size);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("data", enrollmentsPage.getContent());
            response.put("currentPage", enrollmentsPage.getNumber());
            response.put("totalItems", enrollmentsPage.getTotalElements());
            response.put("totalPages", enrollmentsPage.getTotalPages());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "server error",
                            "message", "Error al obtener inscripciones paginadas",
                            "detail", e.getMessage()
                    ));
        }
    }

    // POST
    @PostMapping("/insertCourseEnrollment")
    public ResponseEntity<Map<String, Object>> insertEnrollment(@Valid @RequestBody CourseEnrollmentDTO dto, BindingResult binding) {
        if (binding.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            binding.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("status", "error", "errors", errors));
        }
        try {
            CourseEnrollmentDTO answer = service.insertEnrollment(dto);
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
    @PutMapping("/updateCourseEnrollment/{id}")
    public ResponseEntity<Map<String, Object>> updateEnrollment(@PathVariable String id, @Valid @RequestBody CourseEnrollmentDTO dto, BindingResult binding) {
        if (binding.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            binding.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("status", "error", "errors", errors));
        }
        try {
            CourseEnrollmentDTO answer = service.updateEnrollment(id, dto);
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
    @DeleteMapping("/deleteCourseEnrollment/{id}")
    public ResponseEntity<Map<String, Object>> deleteEnrollment(@PathVariable String id) {
        try {
            if (!service.deleteEnrollment(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "status", "error",
                                "message", "No se encontró la inscripción",
                                "timestamp", Instant.now().toString()
                        ));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("status", "success", "message", "Inscripción eliminada con éxito"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "server error", "message", "Error interno del servidor", "detail", e.getMessage()));
        }
    }
}
