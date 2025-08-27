package ptc2025.backend.Controller.Users;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Entities.Users.UsersEntity;
import ptc2025.backend.Models.DTO.Users.UsersDTO;
import ptc2025.backend.Services.Users.UsersService;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/Users")
@CrossOrigin
public class UsersController {

    @Autowired
    private UsersService services;

    @GetMapping("/getAllUsers")
    public List<UsersDTO> getdata() {
        return services.getAllUsers();
    }

    @PostMapping("/AddUser")
    public ResponseEntity<?> nuevoUsuario(@Valid @RequestBody UsersDTO json, HttpServletRequest request){
        try {
            UsersDTO respuesta = services.insertarDatos(json);
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

    @PutMapping("/UpdateUser/{id}")
    public ResponseEntity<?> modificarUsuario(
            @PathVariable String id,
            @Valid @RequestBody UsersDTO json,
            BindingResult bindingResult
    ){
        if (bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }
        try{
            UsersDTO dto = services.actualizarDatos(id, json);
            return ResponseEntity.ok(dto);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "error",
                    "message", "Registro no encontrado",
                    "detail", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/DeleteUser/{id}")
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
    @GetMapping("/findUsersById/{id}")
    public ResponseEntity<UsersDTO> getUserId(@PathVariable String id){
        Optional<UsersDTO> dto = services.findById(id);
        if(dto.isEmpty()){
            return ResponseEntity.notFound().build();
        }else {
            return ResponseEntity.ok(dto.get());
        }
    }

}

