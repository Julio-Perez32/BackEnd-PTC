package ptc2025.backend.Controller.Faculties;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Models.DTO.Faculties.FacultiesDTO;
import ptc2025.backend.Services.Faculties.FacultiesService;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/Faculties")
@CrossOrigin
public class FacultiesController {

    @Autowired
    FacultiesService service;

    @GetMapping("/getFaculties")
    public List<FacultiesDTO> getFaculties(){return service.getAllFaculties();}

    @GetMapping("/getFacultiesPagination")
    public ResponseEntity<Page<FacultiesDTO>> getFacultiesPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<FacultiesDTO> levels = service.getFacultiesPagination(page, size);

        if(levels.isEmpty()){
            return ResponseEntity.badRequest().body(Page.empty());
        }
        return ResponseEntity.ok(levels);
    }

    @PostMapping("/newFaculties")
    public ResponseEntity<?> newFaculty(@Valid @RequestBody FacultiesDTO json, HttpServletRequest request){
        try{
            FacultiesDTO response = service.insertFaculty(json);
            if(response == null){
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "Insert Failed",
                        "errorType","VALIDATION_ERROR",
                        "message","Data can not be registered"
                ));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "Success",
                    "data", response
            ));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error",
                    "message", "Not controlled error occured",
                    "detail", e.getMessage()
            ));
        }
    }
    @PutMapping("/updateFaculty/{id}")
    public ResponseEntity<?> updateFaculty(@PathVariable String id, @Valid @RequestBody FacultiesDTO json, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }
        try{
            FacultiesDTO dto = service.updateFaculty(id, json);
            return ResponseEntity.ok(dto);
        }catch (IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "Error", "Datos duplicados",
                    "Campo", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/deleteFaculty/{id}")
    public ResponseEntity<?> deleteFaculty(@PathVariable String id){
        try{
            if(!service.deleteFaculty(id)){
                //Error
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Mensaje Error", "Facultad no encontrada")
                        .body(Map.of(
                                "Error", "Not found",
                                "Mensaje", "La facultad no ha sido encontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }
            //Exit
            return ResponseEntity.ok().body(Map.of(
                    "status", "Proceso completado",
                    "message","Facultad eliminado exitosamente"
            ));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of(
                    "status","Error",
                    "message", "Error al eliminar la facultad",
                    "detail", e.getMessage()
            ));
        }
    }
}
