package ptc2025.backend.Controller.RolePermissions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Models.DTO.RolePermissions.RolePermissionsDTO;
import ptc2025.backend.Services.RolePermissions.RolePermissionsService;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/RolePermissions")
@CrossOrigin
public class RolePermissionsController {

    @Autowired
    private RolePermissionsService service;

    @GetMapping("/getAllRolePermissions")
    public List<RolePermissionsDTO> getDAta() {return service.getRolePermissions();}

    @PostMapping("/AddRolePermission")
    public ResponseEntity<?> nuevoResgistro(@Valid @RequestBody RolePermissionsDTO json, HttpServletRequest request){
        try{
            RolePermissionsDTO respuesta = service.insertarDatos(json);
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

    @PutMapping("/UpdateRolePermission/{id}")
    public ResponseEntity<?> actualizarRegistro(@PathVariable String id, @Valid @RequestBody RolePermissionsDTO json, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }
        try {
            RolePermissionsDTO dto = service.actualizarDatos(id, json);
            return ResponseEntity.ok(dto);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "error",
                    "message", "Registro no encontrado",
                    "detail", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/DeleteRolePermission/{id}")
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
