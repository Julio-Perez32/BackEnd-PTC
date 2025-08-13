package ptc2025.backend.Controller.SecurityAnswers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Models.DTO.SecurityAnswers.SecurityAnswersDTO;
import ptc2025.backend.Services.SecurityAnswers.SecurityAnswerService;

import java.util.List;

@RestController
@RequestMapping("/securityAnswers")
public class SecurityAnswersController {
    @Autowired
    private SecurityAnswerService service;

    @GetMapping("/GetSecurityAnswers")
    public List<SecurityAnswersDTO> getData() {return service.getSecurityAnswers();}

   // @PostMapping("/RegistrarDatos")
   // public ResponseEntity<?> ingresarDatos(@Valid @PathVariable SecurityAnswersDTO json, HttpServletRequest request){

    //}
}
