package ptc2025.backend.Controller.requirements;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Models.DTO.Requirements.RequirementsDTO;
import ptc2025.backend.Services.Requirements.RequirementService;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Requirements")
public class RequirementsController {

    @Autowired
    private RequirementService service;

    @GetMapping("/RequirementsgetAlldata")
    public List<RequirementsDTO> getData() {return service.getAllRequirements(); }

    @PostMapping("/RegistroRequirements")
    public ResponseEntity<?> nuevoRequirement(@Valid @RequestBody RequirementsDTO json, HttpServletRequest request){
        try {
            RequirementsDTO respuesta = service.insertarDatos(json);
            if (respuesta == null){
                return ResponseEntity.badRequest().body(Map.of(
                    "status", "insercion fallida",
                    "errorType", "VALIDATION_ERROR",
                    "message", "Los datos no pudieron ser actualizados"
                ));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "status", "succes",
                "message", "Los registros fueron agregados correctamente"
            ));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                        "status", "error",
                            "message", "Error no controlado en el proceso del registro",
                            "detail", e.getMessage()
                    ));
        }
    }
    @PutMapping("/EditarRequirement/{id}")
    public ResponseEntity<?> modificarRequirement(@PathVariable String id, @Valid @RequestBody RequirementsDTO json, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }
        try {
            RequirementsDTO dto = service.actualizarRequirements(id, json);
            return ResponseEntity.ok(dto);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "error",
                    "message", "Registro no encontrado",
                    "detail", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/EliminarRequirement/{id}")
    public ResponseEntity<?> eliminarRequirement(@PathVariable String id){
        try {
            if (!service.eliminarRequirement(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Mensaje error", "Requirement no entcontrado")
                        .body(Map.of(
                            "Error", "not found",
                            "Mensaje", "El registro no fue encontrado",
                            "timestamp", Instant.now().toString()
                        ));

            }
            return ResponseEntity.ok(Map.of(
                "status", "Proceso completado",
                "message", "Registro eliminado con exito"
            ));

        } catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of(
                   "status", "error",
                   "message", "error al eliminar el registro",
                   "detail", e.getMessage()
            ));
        }
    }

}
