package ptc2025.backend.Controller.SecurityQuestions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Models.DTO.SecurityQuestions.SecurityQuestionsDTO;
import ptc2025.backend.Services.SecurityQuestions.SecurityQuestionsService;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/SecurityQuestions")
public class SecurityQuestionsController {

    @Autowired
    private SecurityQuestionsService services;

    @GetMapping("/getAllSecurityQuestions")
    public List<SecurityQuestionsDTO> getData() { return services.getAllSecurityQuestions(); }

    @PostMapping("/AddSecurityQuestion")
    public ResponseEntity<?> nuevaSecurityQuestion(@Valid @RequestBody SecurityQuestionsDTO json, HttpServletRequest request){
        try{
            SecurityQuestionsDTO respuesta = services.insertarDatos(json);
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
                        "status", "error",
                            "message", "Error no controlado al ingresar el nuevo registro",
                            "detail", e.getMessage()
                    ));
        }
    }

    @PutMapping("/UpdateSecurityQuestion/{id}")
    public ResponseEntity<?> modificarSecurityQuestion(@PathVariable String id, @Valid @RequestBody SecurityQuestionsDTO json, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }
        try {
            SecurityQuestionsDTO dto = services.actualizarSecurityQuestion(id, json);
            return ResponseEntity.ok(dto);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "error",
                    "message", "Registro no encontrado",
                    "detail", e.getMessage()
            ));
        }
    }
    @DeleteMapping("/DeleteSecurityQuestion/{id}")
    public ResponseEntity<?> eliminarSecurityQuestion(@PathVariable String id){
        try {
            if (!services.eliminarSecurityQuestion(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Mensaje error", "SecurityQuestion no encontrada")
                        .body(Map.of(
                            "Error", "Not found",
                            "Mensaje", "El registro no fue encontrado",
                            "timestamp", Instant.now().toString()
                        ));
            }
            return ResponseEntity.ok(Map.of(
                "status", "Proceso completado",
                "message", "Registro eliminado exitosamente"
            ));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of(
                "status", "error",
                "message", "Error al eliminar el registro",
                "detail", e.getMessage()
            ));
        }
    }
}
