package ptc2025.backend.Controller.FacultyDeans;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Exceptions.ExcepcionDatosDuplicados;
import ptc2025.backend.Models.DTO.FacultyDeans.FacultyDeansDTO;
import ptc2025.backend.Services.FacultyDeans.FacultyDeansService;
import ptc2025.backend.Services.FacultyLocalities.FacultyLocalitiesService;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/FacultyDeans")
@CrossOrigin
public class FacultyDeansController {

    @Autowired
    private FacultyDeansService service;

    @GetMapping("/GetAllFacultyDeans")
    public List<FacultyDeansDTO> obtenerDatos() { return service.obtenerDatos();}

    @PostMapping("/addFacultyDean")
    public ResponseEntity<?> nuevoRegistro(@Valid @RequestBody FacultyDeansDTO json, HttpServletRequest request){
        try {
            FacultyDeansDTO respuesta = service.insertarDatos(json);
            if (respuesta == null){
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "insercion fallida",
                        "errorType", "VALIDATION_ERROR",
                        "message", "los datos no pudieron ser registrados"
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
                            "message", "Error no controlado al ingresar el nuevo registro",
                            "detail", e.getMessage()
                    ));
        }
    }

    @PutMapping("/UpdateFacultyDean/{id}")
    public ResponseEntity<?> ModificarDato(
            @PathVariable String id,
            @Valid @RequestBody FacultyDeansDTO json,
            BindingResult bindingResult
    ){
        if (bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }
        try {
            FacultyDeansDTO dto = service.actualizarDatos(id, json);
            return ResponseEntity.ok(dto);
        }catch (IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }catch (ExcepcionDatosDuplicados e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("Error", "DatosDuplicados", "Campo", e.getCampoDuplicado())
            );
        }
    }

    @DeleteMapping("/DeleteFacultyDeans/{id}")
    public ResponseEntity<?> eliminarRegistro(@PathVariable String id){
        try {
            if (!service.EliminarDatos(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Mensaje error", "Registro no encontrado")
                        .body(Map.of(
                                "Error", "Not found",
                                "Mensaje", "El registro no fue encontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }

            return ResponseEntity.ok(Map.of(
                    "status", "Proceso Completado",
                    "message", "Registro eliminado exitosamente"
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
