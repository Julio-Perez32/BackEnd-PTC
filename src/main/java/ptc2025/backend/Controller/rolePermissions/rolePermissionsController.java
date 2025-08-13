package ptc2025.backend.Controller.rolePermissions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Models.DTO.rolePermissions.rolePermissionsDTO;
import ptc2025.backend.Services.rolePermissions.rolePermissionService;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rolePermissions")
public class rolePermissionsController {

    @Autowired
    private rolePermissionService service;

    @GetMapping("/getRolePermissions")
    public List<rolePermissionsDTO> getDAta() {return service.getRolePermissions();}

    @PostMapping("/AgregarRegistro")
    public ResponseEntity<?> nuevoResgistro(@Valid @RequestBody rolePermissionsDTO json, HttpServletRequest request){
        try{
            rolePermissionsDTO respuesta = service.insertarDatos(json);
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "Error",
                            "message", "Errorr no controlado al ingresar el nuevo registro",
                            "detail", e.getMessage()
                    ));

        }
    }

    @PutMapping("/EditarRegistro/{id}")
    public ResponseEntity<?> actualizarRegistro(@PathVariable String id, @Valid @RequestBody rolePermissionsDTO json, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }
        try {
            rolePermissionsDTO dto = service.actualizarDatos(id, json);
            return ResponseEntity.ok(dto);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "error",
                    "message", "Registro no encontrado",
                    "detail", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/EliminarRegistro/{id}")
    public ResponseEntity<?> eliminarRegistro(@PathVariable String id){
        try {
            if (!service.eliminarDatos(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Mensaje Error", "Registro no encontrado")
                        .body(Map.of(
                            "Error", "Not found",
                            "Mensaje", "El registro no fue encontrado",
                            "timestamp", Instant.now().toString()
                        ));
            }
            return ResponseEntity.ok(Map.of(
                "status", "Proceso completado",
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
