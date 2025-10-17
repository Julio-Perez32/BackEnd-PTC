package ptc2025.backend.Controller.Pensum;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Models.DTO.Pensum.PensumDTO;
import ptc2025.backend.Services.Pensum.PensumService;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/Pensum")
@CrossOrigin
public class PensumController {

    @Autowired
    PensumService service;

    @GetMapping("/getPensa")
    public ResponseEntity<List<PensumDTO>> getPensa(){
        try {
            List<PensumDTO> pensa = service.getPensa();
            return ResponseEntity.ok(pensa);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }

    @GetMapping("/getPensumPagination")
    public ResponseEntity<Page<PensumDTO>> getPensumPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<PensumDTO> levels = service.getPensumPagination(page, size);

        if(levels.isEmpty()){
            return ResponseEntity.badRequest().body(Page.empty());
        }
        return ResponseEntity.ok(levels);
    }

    @PostMapping("/newPensum")
    public ResponseEntity<?> newPensum(@Valid @RequestBody PensumDTO json, BindingResult bindingResult, HttpServletRequest request){
        // ✅ Verificar errores de validación PRIMERO
        if(bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "Validación fallida",
                    "errorType", "VALIDATION_ERROR",
                    "errors", errores
            ));
        }

        try{
            PensumDTO response = service.insertPensum(json);
            if(response == null){
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "Inserción fallida",
                        "errorType", "VALIDATION_ERROR",
                        "message", "Los datos no pudieron ser registrados"
                ));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "Success",
                    "data", response
            ));
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "Error de validación",
                    "errorType", "VALIDATION_ERROR",
                    "message", e.getMessage()
            ));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error",
                    "message", "Error no controlado al registrar el pensum.",
                    "detail", e.getMessage()
            ));
        }
    }

    @PutMapping("/updatePensum/{id}")
    public ResponseEntity<?> updatePensum(@PathVariable String id, @Valid @RequestBody PensumDTO json, BindingResult bindingResult){
        // ✅ Verificar errores ANTES de actualizar
        if(bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "Validación fallida",
                    "errorType", "VALIDATION_ERROR",
                    "errors", errores
            ));
        }

        try{
            PensumDTO dto = service.updatePensum(id, json);
            return ResponseEntity.ok(Map.of(
                    "status", "Success",
                    "data", dto
            ));
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Not Found",
                    "message", "Pensum no encontrado",
                    "detail", e.getMessage()
            ));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "error", "Error al actualizar",
                    "detail", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/deletePensum/{id}")
    public ResponseEntity<?> deletePensum(@PathVariable String id){
        try{
            if(!service.deletePensum(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Message Error", "Pensum no encontrado")
                        .body(Map.of(
                                "Error", "Not found",
                                "Mensaje", "El pensum no ha sido encontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }
            return ResponseEntity.ok().body(Map.of(
                    "status", "Proceso completado",
                    "message", "Pensum eliminado con exito"
            ));
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar el pensum",
                    "detail", e.getMessage()
            ));
        }
    }
}