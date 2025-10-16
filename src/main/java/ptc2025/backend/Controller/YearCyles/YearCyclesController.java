package ptc2025.backend.Controller.YearCyles;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SimpleErrors;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Exceptions.ExcepcionDatosDuplicados;
import ptc2025.backend.Models.DTO.Users.UsersDTO;
import ptc2025.backend.Models.DTO.YearCycles.YearCyclesDTO;
import ptc2025.backend.Services.YearCycles.YearCyclesService;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/YearCycles")
@CrossOrigin
public class YearCyclesController {

    @Autowired
    YearCyclesService service;

    @GetMapping("/getAllYearCycles")
    public List<YearCyclesDTO> obtenerDatos() { return service.obtenerRegistros();}

    @GetMapping("/getYearCyclesPagination")
    public ResponseEntity<Page<YearCyclesDTO>> getYearCyclesPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<YearCyclesDTO> levels = service.getYearCyclesPagination(page, size);

        if(levels.isEmpty()){
            return ResponseEntity.badRequest().body(Page.empty());
        }
        return ResponseEntity.ok(levels);
    }


    @PostMapping("/addYearCycle")
    public ResponseEntity<?> nuevoRegistro(@Valid @RequestBody YearCyclesDTO json, HttpServletRequest request){
        try {
            YearCyclesDTO respuesta = service.insertarDatos(json);
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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "Error",
                            "message", "Error no controlado al registrar usuario",
                            "detail", e.getMessage()
                    ));
        }
    }
    @PutMapping("/UpdateYearCycle/{id}")
    public ResponseEntity<?> modificarRegistro(
            @PathVariable String id,
            @Valid @RequestBody YearCyclesDTO json,
            BindingResult bindingResult
    ){
        if (bindingResult.hasErrors()){
            Map<String, String> errorres = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errorres.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errorres);
        }
        try {
            YearCyclesDTO dto = service.ActualizarRegistro(id, json);
            return ResponseEntity.ok(dto);
        }catch (IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }catch (ExcepcionDatosDuplicados e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("Error", "DatosDuplicados", "Campo", e.getCampoDuplicado())
            );
        }
    }

    @DeleteMapping("/DeleteYearCycle/{id}")
    public ResponseEntity<?> eliminarRegistro(@PathVariable String id){
        try {
            if (!service.removerRegistro(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Mensaje error", "Registro no encontrado")
                        .body(Map.of(
                                "Error", "Not found",
                                "Mensaje", "El registro no fue encontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }

            return ResponseEntity.ok(Map.of(
                    "status", "Proceso completado",
                    "message", "Registro eliminado completamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Error al eliminar el registro",
                    "detail", e.getMessage()
            ));
        }
    }

   /**
    @GetMapping("/GetDataYearCycles")
    private ResponseEntity<List<YearCyclesDTO>> getData(){
        List<YearCyclesDTO> categories = service.getAllCategories();
        if (categories == null){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "No hay YearCycles registrados"
            ));
        }
        return ResponseEntity.ok(categories);
    }
**/
}
