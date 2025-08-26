package ptc2025.backend.Controller.SystemPermissions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Exceptions.ExcepcionDatosDuplicados;
import ptc2025.backend.Models.DTO.SystemPermissions.SystemPermissionsDTO;
import ptc2025.backend.Services.SystemPermissions.SystemPermissionsService;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/systemPermissions")
@CrossOrigin
public class SystemPermissionsController {

    @Autowired
    private SystemPermissionsService service;

    @GetMapping("/getSystemPermissions")
    List<SystemPermissionsDTO> getData(){ return service.GetsystemPermissions();}

    @PostMapping("/AddSystemPermission")
    public ResponseEntity<?> nuevoResgistro(@Valid @RequestBody SystemPermissionsDTO json, HttpServletRequest request){
        try {
            SystemPermissionsDTO respuesta = service.insertarDatos(json);
            if (respuesta == null){
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "insercion fallida",
                        "errortype", "VALIDATION_ERROR",
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
                            "message", "Error no controlado al hacer el Registro",
                            "detail", e.getMessage()
                    ));
        }
    }


    @PutMapping("/UpdateSystemPermission/{id}")
    public ResponseEntity<?> modificarRegistro(
            @PathVariable String id,
            @Valid @RequestBody SystemPermissionsDTO json,
            BindingResult bindingResult
    ){
        if (bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }
        try {
            SystemPermissionsDTO dto = service.ActualizarDatos(id, json);
            return ResponseEntity.ok(dto);
        }catch (IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }catch (ExcepcionDatosDuplicados e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("Error", "DatosDupliccados", "Campo", e.getCampoDuplicado())
            );
        }
    }

    @DeleteMapping("/DeleteSystemPermission/{id}")
    public ResponseEntity<?> eliminarDatos(@PathVariable String id){
        try {
            if (!service.eliminarRegistro(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Mensaje error", "Dato no encontrado")
                        .body(Map.of(
                                "Error", "Not Found",
                                "Mensaje", "El uuario no ha sido encontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }
            return ResponseEntity.ok(Map.of(
                    "status", "Proceso Completado",
                    "message", "Dato eliminado con exito"
            ));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Error al eliminar el dato",
                    "detail", e.getMessage()
            ));
        }
    }

}
