package ptc2025.backend.Controller.degreeTypes;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Models.DTO.degreeTypes.DegreeTypesDTO;
import ptc2025.backend.Services.degreeTypes.DegreeTypeService;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/DegreeTypes")
public class DegreeTypesController {
    @Autowired
    private DegreeTypeService service;

    @GetMapping("/getDegreeTypes")
    public List<DegreeTypesDTO> getData() { return service.getAllDegreeTypes(); }

    @PostMapping("/RegistroDegreeTypes")
    public ResponseEntity<?> newDegreeType(@Valid @RequestBody DegreeTypesDTO json, HttpServletRequest request){
        try{
            DegreeTypesDTO respuesta = service.insertarDatos(json);
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
                        "status", "Error",
                        "message", "Error no controlado",
                        "detail", e.getMessage()
                ));
        }
    }

    @PutMapping("/editarDegreeType/{id}")
    public ResponseEntity<?> momdificarDegreeType(
            @PathVariable String id,
            @Valid @RequestBody DegreeTypesDTO json,
            BindingResult bindingResult
    ){
        if (bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }
        try{
            DegreeTypesDTO dto = service.actualizarDegreeType(id, json);
            return ResponseEntity.ok(dto);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "error",
                    "message", "Universidad no encontrada",
                    "detail", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/EliminarRegistro/{id}")
    public ResponseEntity<?> eliminarDegreeType (@PathVariable String id){
        try{
            if (!service.eliminarDegreeType(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Mensaje error", "DegreeType no encontrado")
                        .body(Map.of(
                                "Error", "Not found",
                                "Mensaje", "DegreeType no fue encontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }

            return ResponseEntity.ok(Map.of(
                    "status", "Proceso completado",
                    "message", "Registro eliminado"
            ));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Error al eliminar el DegreeType",
                    "detail", e.getMessage()
            ));
        }
    }
}
