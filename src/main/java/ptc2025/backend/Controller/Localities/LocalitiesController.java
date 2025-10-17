package ptc2025.backend.Controller.Localities;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Models.DTO.Localities.LocalitiesDTO;
import ptc2025.backend.Services.Localities.LocalitiesService;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/Locality")
@CrossOrigin
public class LocalitiesController {
    @Autowired
    LocalitiesService service;

    @GetMapping("/getDataLocality")
    public ResponseEntity<List<LocalitiesDTO>> getLocality(){
        try {
            List<LocalitiesDTO> localities = service.getLocalitiesService();
            return ResponseEntity.ok(localities);
        } catch (Exception e) {
            log.error("Error al obtener localidades", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getLocalitiesPagination")
    public ResponseEntity<Page<LocalitiesDTO>> getLocalitiesPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<LocalitiesDTO> levels = service.getLocalitiesPagination(page, size);

        if(levels.isEmpty()){
            return ResponseEntity.ok(Page.empty());
        }
        return ResponseEntity.ok(levels);
    }
    @PostMapping("/newLocality")
    public ResponseEntity<Map<String, Object>> registrarLocalidad(
            @Valid @RequestBody LocalitiesDTO dtoLocal,
            BindingResult result,
            HttpServletRequest  request){

        if(result.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "errorType", "VALIDATION_ERROR",
                    "errors", errores
            ));
        }

        try{
            LocalitiesDTO respuesta = service.insertarLocalidad(dtoLocal);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "success",
                    "data", respuesta
            ));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error al registrar la localidad",
                    "detail", e.getMessage()
            ));
        }
    }
    @PutMapping("/updateLocality/{id}")
    public ResponseEntity<Map<String, Object>> modificarLocalidad(
            @PathVariable String id,
            @Valid @RequestBody LocalitiesDTO dto,
            BindingResult result){

        if(result.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "errorType", "VALIDATION_ERROR",
                    "errors", errores
            ));
        }

        try{
            LocalitiesDTO actualizado = service.modificarLocalidad(id, dto);
            return ResponseEntity.ok().body(Map.of(
                    "status", "success",
                    "data", actualizado
            ));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "error",
                    "message", "Localidad no encontrada",
                    "detail", e.getMessage()
            ));
        }
    }
    @DeleteMapping("/deleteLocality/{id}")
    public ResponseEntity<Map<String, Object>> eliminarLocalidad(@PathVariable String id){
        try{
            if(!service.eliminarLocalidad(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Message-Error", "Localidad no encontrada")
                        .body(Map.of(
                                "error", "Not found",
                                "message", "La localidad no fue encontrada",
                                "timestamp", Instant.now().toString()
                        ));

            }
            return ResponseEntity.ok().body(Map.of(
                    "status", "success",
                    "message", "Localidad eliminada exitosamente"
            ));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar localidad",
                    "detail", e.getMessage()
            ));
        }
    }
}
