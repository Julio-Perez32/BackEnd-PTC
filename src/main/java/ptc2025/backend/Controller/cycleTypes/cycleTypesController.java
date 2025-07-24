package ptc2025.backend.Controller.cycleTypes;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Models.DTO.cycleTypes.cycleTypesDTO;
import ptc2025.backend.Services.cycleTypes.cycleTypeService;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cycleTypes")
public class cycleTypesController {

    @Autowired
    private cycleTypeService service;

    @GetMapping("/getDataCycleTypes")
    public List<cycleTypesDTO> getdata()
    {
        return service.getAllCycleTypes();
    }

    @PostMapping("/RegistroCycleTypes")
    public ResponseEntity<?> nuevoUsuario(@Valid @RequestBody cycleTypesDTO json, HttpServletRequest request){
        try{
            cycleTypesDTO respuesta = service.insertarCycleType(json);
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
    @PutMapping("/UpdateCycleTypes/{id}")
    public ResponseEntity<?> modicarCycleType(@PathVariable String id, @Valid @RequestBody cycleTypesDTO json, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return  ResponseEntity.badRequest().body(errores);
        }
        try {
            cycleTypesDTO dto = service.actualizarCycleTypes(id, json);
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
