package ptc2025.backend.Controller.StudentEvaluations;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Exceptions.ExcepcionDatosDuplicados;
import ptc2025.backend.Models.DTO.StudentEvaluations.StudentEvaluationsDTO;
import ptc2025.backend.Models.DTO.YearCycles.YearCyclesDTO;
import ptc2025.backend.Services.StudentEvaluations.StudentEvaluationsService;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/StudentEvaluations")
public class StudentEvaluationsController {
    @Autowired
    StudentEvaluationsService service;

    @GetMapping("/GetAllStudentsEvaluations")
    public List<StudentEvaluationsDTO> obtenerDatos(){return service.getAllStudentsEvaluations();}

    @PostMapping("/AddStudentEvaluation")
    public ResponseEntity<?> AgregarRegistro(@Valid @RequestBody StudentEvaluationsDTO json, HttpServletRequest request){
        try{
            StudentEvaluationsDTO respuesta = service.insertarDatos(json);
            if (respuesta == null){
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "insercion fallida",
                        "message", "error no controlado al registrar el nuevo dato",
                        "detail", "Los datos no pudieron ser registrados"
                ));
            }
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "status", "succes",
                "data", respuesta
        ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "Error",
                            "message", "Error no controlado al registrar usuario",
                            "detail", e.getMessage()
                    ));
        }
    }

    @PutMapping("/UpdateStudentEvaluation/{id}")
    public ResponseEntity<?> ModificarRegistro(
            @PathVariable String id,
            @Valid @RequestBody StudentEvaluationsDTO json,
            BindingResult bindingResult
    ){
        if (bindingResult.hasErrors()){
            Map<String, String> errorres = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errorres.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errorres);
        }
        try {
            StudentEvaluationsDTO dto = service.ActualizarRegistro(id, json);
            return ResponseEntity.ok(dto);
        }catch (IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }catch (ExcepcionDatosDuplicados e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("Error", "DatosDuplicados", "Campo", e.getCampoDuplicado())
            );
        }
    }

    @DeleteMapping("/DeleteStudentEvaluation/{id}")
    public ResponseEntity<?> ElminarRegistro( @PathVariable String id){
        try {
            if (!service.removerRegistro(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Mensaje error", "Registro no encontrado")
                        .body(Map.of(
                                "Error", "Not found",
                                "Mensaje", "El registro no fue encontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }

            return ResponseEntity.ok(Map.of(
                    "status", "Proceso completado",
                    "message", "Registro eliminado completamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Error al eliminar el registro",
                    "detail", e.getMessage()
            ));
        }
    }

}
