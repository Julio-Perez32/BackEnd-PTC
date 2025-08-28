package ptc2025.backend.Controller.StudentCareerEnrollments;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Models.DTO.StudentCareerEnrollments.StudentCareerEnrollmentsDTO;
import ptc2025.backend.Services.StudentCareerEnrollments.StudentCareerEnrollmentsService;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/StudentCareerEnrollments")
@CrossOrigin
public class StudentCareerEnrollmentsController {

    @Autowired
    private StudentCareerEnrollmentsService service;

    @GetMapping("/getAll")
    public List<StudentCareerEnrollmentsDTO> getAll(){
        return service.getAllEnrollments();
    }

    @GetMapping("/getStudentCareerEnrollmentPagination")
    public ResponseEntity<Page<StudentCareerEnrollmentsDTO>> getStudentCareerEnrollmentPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<StudentCareerEnrollmentsDTO> levels = service.getStudentCareerEnrollmentPagination(page, size);

        if(levels.isEmpty()){
            return ResponseEntity.badRequest().body(Page.empty());
        }
        return ResponseEntity.ok(levels);
    }
    @PostMapping("/insert")
    public ResponseEntity<?> insert(@Valid @RequestBody StudentCareerEnrollmentsDTO dto, HttpServletRequest request){
        try{
            StudentCareerEnrollmentsDTO respuesta = service.insertEnrollment(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status","success",
                    "data",respuesta));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error al registrar la inscripción",
                    "detail", e.getMessage()
            ));
        }
    }

    // UPDATE
    @PostMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @Valid @RequestBody StudentCareerEnrollmentsDTO dto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }
        try{
            StudentCareerEnrollmentsDTO updated = service.updateEnrollment(id, dto);
            return ResponseEntity.ok(updated);
        }catch (IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "Error", "Datos duplicados o inválidos",
                    "Campo", e.getMessage()
            ));
        }
    }

    //DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id){
        try{
            if(!service.deleteEnrollment(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "Error", "Not found",
                                "Mensaje", "La inscripción no ha sido encontrada",
                                "timestamp", Instant.now().toString()
                        ));
            }
            return ResponseEntity.ok().body(Map.of(
                    "status", "Proceso completado",
                    "message", "Inscripción eliminada con éxito"
            ));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar la inscripción",
                    "detail", e.getMessage()
            ));
        }
    }
}
