package ptc2025.backend.Controller.securityQuestions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ptc2025.backend.Models.DTO.securityQuestions.securityQuestionsDTO;
import ptc2025.backend.Services.securityQuestions.securityQuestionService;

import java.util.List;

@RestController
@RequestMapping("/securityQuestions")
public class securityQuestionsController {

    @Autowired
    private securityQuestionService services;

    @GetMapping("/getAllSecurityQuestions")
    public List<securityQuestionsDTO> getData() { return services.getAllSecurityQuestions(); }
}
