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
import ptc2025.backend.Exceptions.ExceptionBadRequest;
import ptc2025.backend.Exceptions.ExceptionNoSuchElement;
import ptc2025.backend.Models.DTO.Localities.LocalitiesDTO;
import ptc2025.backend.Services.Localities.LocalitiesService;

import java.time.Instant;
import java.util.ArrayList;
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
            log.info("GET /getDataLocality - Obteniendo todas las localidades");
            List<LocalitiesDTO> localities = service.getLocalitiesService();
            log.info("GET /getDataLocality - Retornando {} localidades", localities.size());
            return ResponseEntity.ok(localities);
        } catch (Exception e) {
            log.error("GET /getDataLocality - Error al obtener localidades", e);
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
            HttpServletRequest request){

        log.info("POST /newLocality - Recibiendo solicitud");
        log.info("POST /newLocality - Datos recibidos: universityID={}, address={}, phone={}, isMain={}",
                dtoLocal.getUniversityID(),
                dtoLocal.getAddress(),
                dtoLocal.getPhoneNumber(),
                dtoLocal.getIsMainLocality());

        // Validar errores de validación de campos
        if(result.hasErrors()){
            log.warn("POST /newLocality - Errores de validación encontrados");
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error -> {
                log.warn("POST /newLocality - Error en campo {}: {}", error.getField(), error.getDefaultMessage());
                errores.put(error.getField(), error.getDefaultMessage());
            });

            // Formato compatible con Network.js: { message, errors: [] }
            List<String> errorList = new ArrayList<>();
            errores.forEach((field, msg) -> errorList.add(field + ": " + msg));

            return ResponseEntity.badRequest().body(Map.of(
                    "message", "Error de validación",
                    "errors", errorList
            ));
        }

        try{
            LocalitiesDTO respuesta = service.insertarLocalidad(dtoLocal);
            log.info("POST /newLocality - Localidad creada exitosamente: {}", respuesta.getLocalityID());
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "success",
                    "data", respuesta
            ));
        } catch (ExceptionBadRequest e){
            log.error("POST /newLocality - Error de validación de negocio: {}", e.getMessage());
            // Formato compatible con Network.js
            return ResponseEntity.badRequest().body(Map.of(
                    "message", e.getMessage(),
                    "errors", List.of(e.getMessage())
            ));
        } catch (ExceptionNoSuchElement e){
            log.error("POST /newLocality - Elemento no encontrado: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message", e.getMessage(),
                    "errors", List.of(e.getMessage())
            ));
        } catch (Exception e){
            log.error("POST /newLocality - Error interno del servidor", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "message", "Error interno al registrar la localidad",
                    "errors", List.of(e.getMessage() != null ? e.getMessage() : "Error desconocido")
            ));
        }
    }

    @PutMapping("/updateLocality/{id}")
    public ResponseEntity<Map<String, Object>> modificarLocalidad(
            @PathVariable String id,
            @Valid @RequestBody LocalitiesDTO dto,
            BindingResult result){

        log.info("PUT /updateLocality/{} - Recibiendo solicitud", id);
        log.info("PUT /updateLocality/{} - Datos: universityID={}, address={}, phone={}, isMain={}",
                id, dto.getUniversityID(), dto.getAddress(), dto.getPhoneNumber(), dto.getIsMainLocality());

        if(result.hasErrors()){
            log.warn("PUT /updateLocality/{} - Errores de validación", id);
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error -> {
                log.warn("PUT /updateLocality/{} - Error en campo {}: {}", id, error.getField(), error.getDefaultMessage());
                errores.put(error.getField(), error.getDefaultMessage());
            });

            List<String> errorList = new ArrayList<>();
            errores.forEach((field, msg) -> errorList.add(field + ": " + msg));

            return ResponseEntity.badRequest().body(Map.of(
                    "message", "Error de validación",
                    "errors", errorList
            ));
        }

        try{
            LocalitiesDTO actualizado = service.modificarLocalidad(id, dto);
            log.info("PUT /updateLocality/{} - Localidad actualizada exitosamente", id);
            return ResponseEntity.ok().body(Map.of(
                    "status", "success",
                    "data", actualizado
            ));
        } catch (ExceptionNoSuchElement e){
            log.error("PUT /updateLocality/{} - Localidad no encontrada: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message", e.getMessage(),
                    "errors", List.of(e.getMessage())
            ));
        } catch (ExceptionBadRequest e){
            log.error("PUT /updateLocality/{} - Error de validación: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                    "message", e.getMessage(),
                    "errors", List.of(e.getMessage())
            ));
        } catch (Exception e){
            log.error("PUT /updateLocality/{} - Error interno", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "message", "Error al actualizar la localidad",
                    "errors", List.of(e.getMessage() != null ? e.getMessage() : "Error desconocido")
            ));
        }
    }

    @DeleteMapping("/deleteLocality/{id}")
    public ResponseEntity<Map<String, Object>> eliminarLocalidad(@PathVariable String id){
        log.info("DELETE /deleteLocality/{} - Recibiendo solicitud", id);
        try{
            if(!service.eliminarLocalidad(id)){
                log.warn("DELETE /deleteLocality/{} - Localidad no encontrada", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "message", "La localidad no fue encontrada",
                        "errors", List.of("Localidad no encontrada con ID: " + id),
                        "timestamp", Instant.now().toString()
                ));
            }
            log.info("DELETE /deleteLocality/{} - Localidad eliminada exitosamente", id);
            return ResponseEntity.ok().body(Map.of(
                    "status", "success",
                    "message", "Localidad eliminada exitosamente"
            ));
        }catch (Exception e){
            log.error("DELETE /deleteLocality/{} - Error al eliminar", id, e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "message", "Error al eliminar localidad",
                    "errors", List.of(e.getMessage() != null ? e.getMessage() : "Error desconocido")
            ));
        }
    }
}