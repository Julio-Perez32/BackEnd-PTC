package ptc2025.backend.Controller.EvaluationPlans;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Exceptions.ExceptionNoSuchElement;
import ptc2025.backend.Models.DTO.EvaluationPlans.EvaluationPlansDTO;
import ptc2025.backend.Services.EvaluationPlans.EvaluationPlansService;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/EvaluationPlans")
@CrossOrigin
public class EvaluationPlansController {
    @Autowired
    private EvaluationPlansService service;

    @GetMapping("/getEvaluationPlan")
    public ResponseEntity<?> getEvaluationPlan() {
        try {
            List<EvaluationPlansDTO> list = service.getEvaluationPlans();
            if (list == null || list.isEmpty()) return ResponseEntity.noContent().build(); // 204
            return ResponseEntity.ok(list); // 200
        } catch (ExceptionNoSuchElement ex) {              // <- si el service la lanza
            return ResponseEntity.noContent().build();     // 204 en vez de 500
        }
    }

    @GetMapping("/getEvaluationPlansPagination")
    public ResponseEntity<Page<EvaluationPlansDTO>> getEvaluationPlansPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        // NO 400 si viene vacío; devuelve 200 con Page vacío
        Page<EvaluationPlansDTO> p = service.getEvaluationPlansPagination(page, size);
        return ResponseEntity.ok(p);
    }



    @PostMapping("/insertEvaluationPlan")
    public ResponseEntity<Map<String, Object>> insertEvaluationPlan(@Valid @RequestBody EvaluationPlansDTO dto, HttpServletRequest request){
        try{
            EvaluationPlansDTO answer = service.insertEvaluationPlan(dto);
            if(answer==null){
                return ResponseEntity.status(HttpStatus. BAD_REQUEST)
                        .body(Map.of(
                            "status", "error",
                            "error type", "VALIDATION_ERROR" ,
                            "message","Error al insertar un plan de evaluación"
                        ));
            }
            return ResponseEntity.status((HttpStatus.CREATED))
                    .body(Map.of(
                            "status", "success",
                            "dara", answer
                    ));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "server error",
                            "message", "Error interno del servidor",
                            "detail", e.getMessage()
                    ));
        }
    }
    @PutMapping("/updateEvaluationPlan/{id}")
    public ResponseEntity<?> updateEvaluationPlan(@PathVariable String id, @Valid @RequestBody EvaluationPlansDTO dto, BindingResult binding ){
        if(binding.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            binding.getFieldErrors().forEach(error ->
                    errors.put(error.getField(),error.
                            getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);

        }
        try{
            EvaluationPlansDTO answer =  service.updateEvaluationPlan(id, dto);
            if(answer == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of(
                                "status", "error",
                                "error type", "VALIDATION_ERROR" ,
                                "message","Error al actualizar un plan de evaluación"
                        ));
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(Map.of(
                            "status", "success",
                            "data", answer
                    ));
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "server error",
                            "message", "Error interno del servidor",
                            "detail", e.getMessage()
                    ));
        }
    }

    @DeleteMapping("/deleteEvaluationPlan/{id}")
    public ResponseEntity<Map<String, Object>> deleteEvaluationPlan(@PathVariable String id){
        try{
            if(!service.deleteEvaluationPlan(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "errror", "not found",
                                "message", "No se encontro el plan de evaluacion",
                                "timestramp", Instant.now().toString()
                        ));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of(
                            "status", "process completed",
                            "message", "Plan de evaluacion eliminado con exito"
                    ));
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "server error",
                            "message", "Error interno del servidor",
                            "detail", e.getMessage()
                    ));
        }
    }
}
