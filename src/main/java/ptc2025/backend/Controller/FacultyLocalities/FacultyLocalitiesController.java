package ptc2025.backend.Controller.FacultyLocalities;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.MappingMatch;
import jakarta.validation.Valid;
import org.hibernate.validator.constraints.pl.REGON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Exceptions.ExcepcionDatosDuplicados;
import ptc2025.backend.Models.DTO.FacultyLocalities.FacultyLocalitiesDTO;
import ptc2025.backend.Services.FacultyLocalities.FacultyLocalitiesService;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/FacultyLocalities")
public class FacultyLocalitiesController {
    @Autowired
    FacultyLocalitiesService service;

    @GetMapping("/GetAllFacultyLocalities")
    public List<FacultyLocalitiesDTO> obtenerDatos(){ return service.getAllFacultyLocalities();}

    @PostMapping("/AddFacultyLocality")
    public ResponseEntity<?> nuevaFacultadLocalidad(@Valid @RequestBody FacultyLocalitiesDTO json, HttpServletRequest request){
        try {
            FacultyLocalitiesDTO respuesta = service.insertarDatos(json);
            if (respuesta == null){
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "insercion fallida",
                        "errorType", "VALIDATION_ERROR",
                        "message", "Lo datos no pudieron ser ingresados"
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
                            "mmessage", "Error no controlado al registrar usuario",
                            "detail", e.getMessage()
                    ));
        }
    }


    @PutMapping("/UpdateFacultyLocality/{id}")
    public ResponseEntity<?> modificarLocalidadDeFacultad(@PathVariable String id,
                                                          @Valid @RequestBody FacultyLocalitiesDTO json,
                                                          BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }
        try {
            FacultyLocalitiesDTO dto = service.actualizarDatos(id, json);
            return ResponseEntity.ok(dto);
        }catch (IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }catch (ExcepcionDatosDuplicados e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("Error", "datos duplicados", "Campo", e.getCampoDuplicado()));
        }
    }

    @DeleteMapping("/DeleteFacultyLocality/{id}")
    public ResponseEntity<?> eliminarRegistro(@PathVariable String id){
        try {
            if (!service.eliminarDato(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Mensaje Error", "Usuario no encontrado")
                        .body(Map.of(
                                "Error", "Not found",
                                "Mensaje", "El registro no ha sido encontrado",
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
                    "message", "Error al eliminar el Registro",
                    "detail", e.getMessage()
            ));
        }
    }

}
