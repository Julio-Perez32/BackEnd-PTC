package ptc2025.backend.Controller.DocumentCategories;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import ptc2025.backend.Models.DTO.DocumentCategories.DocumentCategoriesDTO;
import ptc2025.backend.Services.DocumentCategories.DocumentCategoriesService;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/documentCategories")
@CrossOrigin
public class DocumentCategoriesController {

    @Autowired
    private DocumentCategoriesService services;

    @GetMapping("/getAllDocumentCategories")
    public List<DocumentCategoriesDTO> getData() { return services.getDocumentCategories(); }

    @GetMapping("/getAllDocumentCategoriesPagination")
    public ResponseEntity<Page<DocumentCategoriesDTO>> getDocumentCategoriesPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<DocumentCategoriesDTO> levels = services.getDocumentCategoriesPagination(page, size);

        if(levels.isEmpty()){
            return ResponseEntity.badRequest().body(Page.empty());
        }
        return ResponseEntity.ok(levels);
    }

    @PostMapping("/AddDocumentCategory")
    public ResponseEntity<?> nunevoDocumentCategory(@Valid @RequestBody DocumentCategoriesDTO json, HttpServletRequest request){
        try {
            DocumentCategoriesDTO respuesta = services.insertarDatos(json);
            if (respuesta == null){
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "insercion fallida",
                        "errorType", "VALIDATION_ERROR",
                        "message", "Los datos no pudieron ser registrados"
                ));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "succes",
                    "data", respuesta
            ));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "Error",
                            "message", "Error no controlado al ingresar nuevo dato",
                            "detail", e.getMessage()
                    ));
        }
    }

    @PutMapping("/UpdateDocumentCategory/{id}")
    public ResponseEntity<?> modificarDocumentCategory(@PathVariable String id, @Valid @RequestBody DocumentCategoriesDTO json, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }
        try {
            DocumentCategoriesDTO dto = services.actualizarDocumentCategory(id, json);
            return ResponseEntity.ok(dto);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "error",
                    "message", "Universidad no encontrada",
                    "detail", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/DeleteDocumentCategory/{id}")
    public ResponseEntity<?> eliminarDocumentCategories(@PathVariable String id){
        try{
            if (!services.eliminardocumentCategories(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Mensaje error", "document no entcontrado")
                        .body(Map.of(
                                "Error", "Not found",
                                "Mensaje", "El documento no ha sido encontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }
            return ResponseEntity.ok(Map.of(
               "status", "Proceso Completado",
               "message", "registro eliminado exitosamente"
            ));
        }catch (Exception e){
            return  ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Error al eliminar el registro",
                    "detail", e.getMessage()
            ));
        }
    }
}
