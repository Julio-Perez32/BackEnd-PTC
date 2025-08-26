package ptc2025.backend.Controller.Documents;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Models.DTO.Documents.DocumentDTO;
import ptc2025.backend.Services.Documents.DocumentService;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/Documents")
@CrossOrigin
public class DocumentController {

    @Autowired
    private DocumentService service;

    //GET
    @GetMapping("/getDocuments")
    public List<DocumentDTO> getDocuments() {
        return service.getDocuments();
    }

    @GetMapping("/getDocumentsPaginated")
    public ResponseEntity<Map<String, Object>> getDocumentsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            var documentsPage = service.getDocumentsPaginated(page, size);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("data", documentsPage.getContent());
            response.put("currentPage", documentsPage.getNumber());
            response.put("totalItems", documentsPage.getTotalElements());
            response.put("totalPages", documentsPage.getTotalPages());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "server error",
                            "message", "Error al obtener documentos paginados",
                            "detail", e.getMessage()
                    ));
        }
    }

    //INSERT
    @PostMapping("/insertDocument")
    public ResponseEntity<Map<String, Object>> insertDocument(
            @Valid @RequestBody DocumentDTO dto,
            HttpServletRequest request) {
        try {
            DocumentDTO answer = service.insertDocument(dto);
            if (answer == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of(
                                "status", "error",
                                "error type", "VALIDATION_ERROR",
                                "message", "Error al insertar documento"
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

    @PutMapping("/updateDocument/{id}")
    public ResponseEntity<?> updateDocument(
            @PathVariable String id,
            @Valid @RequestBody DocumentDTO dto,
            BindingResult binding) {
        if (binding.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            binding.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            DocumentDTO answer = service.updateDocument(id, dto);
            if (answer == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of(
                                "status", "error",
                                "error type", "VALIDATION_ERROR",
                                "message", "Error al actualizar documento"
                        ));
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED)
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

    @DeleteMapping("/deleteDocument/{id}")
    public ResponseEntity<Map<String, Object>> deleteDocument(@PathVariable String id) {
        try {
            if (!service.deleteDocument(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "error", "not found",
                                "message", "No se encontró el documento",
                                "timestamp", Instant.now().toString()
                        ));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of(
                            "status", "process completed",
                            "message", "Documento eliminado con éxito"
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
}