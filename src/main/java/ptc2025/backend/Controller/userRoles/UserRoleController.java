package ptc2025.backend.Controller.userRoles;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Models.DTO.userRoles.UserRoleDTO;
import ptc2025.backend.Services.userRoles.UserRoleService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/userRoles")
@RequiredArgsConstructor
@Slf4j
public class UserRoleController {

    private final UserRoleService servicio;

    @GetMapping("/getRoles")
    public ResponseEntity<List<UserRoleDTO>> obtenerRoles() {
        return ResponseEntity.ok(servicio.obtenerTodos());
    }

    @PostMapping("/insertRole")
    public ResponseEntity<?> insertar(@RequestBody @Valid UserRoleDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "Datos inválidos", "errores", result.getAllErrors()));
        }
        try {
            UserRoleDTO creado = servicio.insertar(dto);
            return ResponseEntity.ok(Map.of("mensaje", "Rol registrado", "data", creado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        }
    }

    @PutMapping("/updateRole/{id}")
    public ResponseEntity<?> actualizar(@PathVariable String id, @RequestBody @Valid UserRoleDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "Datos inválidos", "error", result.getAllErrors()));
        }
        try {
            UserRoleDTO actualizado = servicio.actualizar(id, dto);
            return ResponseEntity.ok(Map.of("mensaje", "Rol actualizado", "data", actualizado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(Map.of("mensaje", "Error al actualizar"));
        }
    }

    @DeleteMapping("/deleteRole/{id}")
    public ResponseEntity<?> eliminar(@PathVariable String id) {
        boolean resultado = servicio.eliminar(id);
        if (resultado) {
            return ResponseEntity.ok(Map.of("mensaje", "Rol eliminado"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "No se encontró el rol"));
        }
    }
}
