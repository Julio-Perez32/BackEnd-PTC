package ptc2025.backend.Controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Models.DTO.UserDTO;
import ptc2025.backend.Services.UserService;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Usuarios")
public class UserController {

    @Autowired
    private UserService services;

    @GetMapping("/getUsers")
    public List<UserDTO> getdata() {
        return services.getAllUsers();
    }

    @PostMapping("/RegistrarUsuario")
    public ResponseEntity<?> nuevoUsuario(@Valid @RequestBody UserDTO json, HttpServletRequest request){
        try {
            UserDTO respuesta = services.insertarDatos(json);
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
                        "message", "Error no controlado al Hacer el nuevo registro",
                        "detail", e.getMessage()
                    ));
        }
    }

    @PutMapping("/editarUsuario/{id}")
    public ResponseEntity<?> modificarUsuario(@PathVariable String id, @Valid @RequestBody UserDTO json, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }
        try{
            UserDTO dto = services.actualizarDatos(id, json);
            return ResponseEntity.ok(dto);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "error",
                    "message", "Registro no encontrado",
                    "detail", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/eliminarUsuario/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable String id){
        try{
            if (!services.eliminarUsuario(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Mensaje error", "Usuario no encontrado")
                        .body(Map.of(
                            "Error", "Not found",
                            "Mensaje", "El usuario no ha sido encontrado",
                            "timestamp", Instant.now().toString()
                        ));
            }
            return ResponseEntity.ok(Map.of(
               "status", "Proceso completado",
               "message", "Usuario eliminado correctamente"
            ));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of(
                "status", "error",
                "message", "Error al eliminar el usuario",
                "detail", e.getMessage()
            ));
        }
    }

}

