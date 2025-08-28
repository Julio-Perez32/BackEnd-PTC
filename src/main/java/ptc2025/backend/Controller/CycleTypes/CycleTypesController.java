package ptc2025.backend.Controller.CycleTypes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Models.DTO.CycleTypes.CycleTypesDTO;
import ptc2025.backend.Services.CycleTypes.CycleTypesService;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/CycleTypes")
@CrossOrigin
public class CycleTypesController {

    @Autowired
    private CycleTypesService service;

    @GetMapping("/getAllCycleTypes")
    public List<CycleTypesDTO> getdata()
    {
        return service.getAllCycleTypes();
    }

    @GetMapping("/getAllCycleTypesPagination")
    public ResponseEntity<Page<CycleTypesDTO>> getCycleTypesPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<CycleTypesDTO> levels = service.getAllCycleTypesPagination(page, size);

        if(levels.isEmpty()){
            return ResponseEntity.badRequest().body(Page.empty());
        }
        return ResponseEntity.ok(levels);
    }

    @PostMapping("/AddCycleType")
    public ResponseEntity<?> nuevoUsuario(@Valid @RequestBody CycleTypesDTO json, HttpServletRequest request){
        try{
            CycleTypesDTO respuesta = service.insertarCycleType(json);
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
        }catch (Exception e){
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                 "status", "Error",
                 "message", "Error nno controlado al registrar tipo de ciclo",
                 "detail", e.getMessage()
         ));
        }
    }
    @PutMapping("/UpdateCycleType/{id}")
    public ResponseEntity<?> modicarCycleType(@PathVariable String id, @Valid @RequestBody CycleTypesDTO json, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return  ResponseEntity.badRequest().body(errores);
        }
        try {
            CycleTypesDTO dto = service.actualizarCycleTypes(id, json);
            return ResponseEntity.ok(dto);
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "error",
                    "message", "cycleType no encontrada",
                    "detail", e.getMessage()
            ));
        }
    }
    @DeleteMapping("/DeleteCycleType/{id}")
    public ResponseEntity<?> eliminarCycleType(@PathVariable String id){
        try{
            if (!service.eliminarCycleType(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Mensaje error", "CycleType no encontrado")
                        .body(Map.of(
                            "Error", "Not found",
                                "Mensaje", "El usuario no ha sido entcontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }
            return ResponseEntity.ok(Map.of(
                    "status", "Proceso completado",
                    "message", "Usuario eliminado exitosamente"
            ));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of(
               "status", "error",
               "message", "Error al eliminar cycleType",
               "detail", e.getMessage()
            ));
        }

    }
}
