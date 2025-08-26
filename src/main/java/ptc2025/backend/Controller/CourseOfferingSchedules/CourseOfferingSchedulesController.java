package ptc2025.backend.Controller.CourseOfferingSchedules;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Models.DTO.CourseOfferingSchedules.CourseOfferingSchedulesDTO;
import ptc2025.backend.Services.CourseOfferingSchedules.CourseOfferingSchedulesService;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courseOfferingSchedules")
@CrossOrigin
public class CourseOfferingSchedulesController {
    @Autowired
    private CourseOfferingSchedulesService service;

    @GetMapping("/GetAllCourseOfferingSchedules")
    public List<CourseOfferingSchedulesDTO> getData(){return service.GetAllcourseOfferingSchedules();}

    @PostMapping("/AddCourseOfferingSchedule")
    public ResponseEntity<?> nuevoCourseOfferingSchedule(@Valid @RequestBody CourseOfferingSchedulesDTO json, HttpServletRequest request){
        try {
            CourseOfferingSchedulesDTO respuesta = service.insertarDatos(json);
            if (respuesta == null){
                return ResponseEntity.badRequest().body(Map.of(
                    "status", "insercion fallida",
                    "errorType", "VALIDATION_ERROR",
                        "message", "Los datos no pudieron ser ingresados"
                ));
            }
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
            "status", "succes",
            "data", respuesta
        ));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                        "status", "Error",
                        "message", "Error no controlado",
                        "detail", e.getMessage()
                    ));
        }
    }
    @PutMapping("/UpdateCourseOfferingSchedule/{id}")
    public ResponseEntity<?> modificarcourseOfferingSchedule(@PathVariable String id, @Valid @RequestBody CourseOfferingSchedulesDTO json, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }
        try {
            CourseOfferingSchedulesDTO dto = service.actualizarDatos(id, json);
            return ResponseEntity.ok(dto);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "error",
                    "message", "Registro no encontrado",
                    "detail", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/DeleteCourseOfferingSchedule/{id}")
    public ResponseEntity<?> eliminarcourseOfferingSchedule(@PathVariable String id){
        try {
            if (!service.eliminarcourseOfferingSchedule(id)){
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
                "message", "Registro eliminado con exito"
            ));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of(
                "status", "Error",
                "message", "Error al eliminar el registro",
                "detail", e.getMessage()
            ));
        }
    }
}
