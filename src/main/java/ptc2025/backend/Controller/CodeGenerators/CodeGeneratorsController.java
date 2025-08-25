package ptc2025.backend.Controller.CodeGenerators;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Models.DTO.CodeGenerators.CodeGeneratorsDTO;
import ptc2025.backend.Respositories.CodeGenerators.CodeGeneratorsRespository;
import ptc2025.backend.Services.CodeGenerators.CodeGeneratorsService;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/CodeGenerators")
public class CodeGeneratorsController {
    @Autowired
    CodeGeneratorsService service;

    @GetMapping("/getCodeGenerators")
    public List<CodeGeneratorsDTO> getCodeGenerators(){
        return service.getCodeGenerator();
    }
    @PostMapping("/NewCodeGenerators")
    public ResponseEntity<?> newCodeGenerators(
            @Valid @RequestBody CodeGeneratorsDTO dto,
            HttpServletRequest request)
    {
        try {
            CodeGeneratorsDTO response = service.insertCodeGenerastor(dto);
            if (response == null){
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "insercion fallida",
                        "errorType", "VALIDATION_ERROR",
                        "message", "Los datos no pudieron ser registrados para hacer el nuevo registro del estandar para generar codigo"
                ));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "succes",
                    "data", response
            ));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "Error",
                            "message", "Error no controlado al hacer el nuevo registro del estandar para generar codigo",
                            "detail", e.getMessage()
                    ));
        }
    }

    @PutMapping("/UpdateCodeGenerator/{id}")
    public ResponseEntity<?> updateCodeGenartor (
            @PathVariable String id,
            @Valid @RequestBody CodeGeneratorsDTO json,
            BindingResult bindingResult
    ){
        if (bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            CodeGeneratorsDTO dto = service.updateCodeGenerator(id, json);
            return ResponseEntity.ok(dto);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "error",
                    "message", "Registro no encontrado",
                    "detail", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/DeleteUser/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable String id){
        try{
            if (!service.deleteCode(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Mensaje error", "El estandar para generar codigo no encontrado")
                        .body(Map.of(
                                "Error", "Not found",
                                "Mensaje", "El estandar para generar codigo no ha sido encontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }
            return ResponseEntity.ok(Map.of(
                    "status", "Proceso completado",
                    "message", "Estandar para generar codigo eliminado correctamente"
            ));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Error al eliminar el estandar para generar codigo",
                    "detail", e.getMessage()
            ));
        }
    }
}
