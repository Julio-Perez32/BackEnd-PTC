package ptc2025.backend.Controller.EvaluationValidations;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Entities.EvaluationValidations.EvaluationValidationsEntity;
import ptc2025.backend.Models.DTO.EvaluationValidations.EvaluationValidationsDTO;
import ptc2025.backend.Services.EvaluationValidations.EvaluationValidationsService;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/EvaluationValidation")
public class EvaluationValidationsController {

    @Autowired
    EvaluationValidationsService service;

    @GetMapping("/getEvaluationValidations")
    public List<EvaluationValidationsDTO> getEvaluationValidations(){return service.getAllValidations();}

    @PostMapping("/newEvaluationValidation")
    public ResponseEntity<?> newEvaluationValidation(@Valid @RequestBody EvaluationValidationsDTO json, HttpServletRequest request){
        try{
            EvaluationValidationsDTO response = service.insertEvaluationValidation(json);
            if(response == null){
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "Inserción fallida",
                        "errorType", "VALIDATION_ERROR",
                        "message", "Los datos no pudierons ser registrados"
                ));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "Success",
                    "data", response
            ));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error",
                    "message", "Error no controlado al registrar la validación",
                    "detail", e.getMessage()
            ));
        }
    }

    @PutMapping("/updateEvaluationValidation/{id}")
    public ResponseEntity<?> updateEvaluationValidation(@PathVariable String id, @Valid @RequestBody EvaluationValidationsDTO json, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }
        try{
            EvaluationValidationsDTO dto = service.updateEvaluationValidation(id, json);
            return ResponseEntity.ok(dto);
        }catch (IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "Error", "Datos Duplicados",
                    "Campo", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/deleteEvaluationValidation/{id}")
    public ResponseEntity<?> deleteEvaluationValidation(@PathVariable String id){
        try{
            if(!service.deleteEvaluationValidation(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Mensaje error", "Validación de evalición no encontrada")
                        .body(Map.of(
                                "Error", "Not Found",
                                "Mensaje", "La validación no pudo ser encontrada",
                                "timestamp", Instant.now().toString()
                        ));
            }
            return ResponseEntity.ok().body(Map.of(
                    "status", "Proceso completado",
                    "message", "Validación eliminada exitosamente"
            ));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar la validación",
                    "detail", e.getMessage()
            ));
        }
    }
}
