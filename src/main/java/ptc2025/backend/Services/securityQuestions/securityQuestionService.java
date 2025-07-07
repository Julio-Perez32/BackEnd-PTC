package ptc2025.backend.Services.securityQuestions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.securityQuestions.securityQuestionsEntity;
import ptc2025.backend.Models.DTO.securityQuestions.securityQuestionsDTO;
import ptc2025.backend.Respositories.securityQuestions.securityQuestionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class securityQuestionService {

    @Autowired
    private securityQuestionRepository repo;

    public List<securityQuestionsDTO> getAllSecurityQuestions(){
        List<securityQuestionsEntity> secuQ = repo.findAll();
        return secuQ.stream()
                .map(this::convertirPreguntasDTO)
                .collect(Collectors.toList());
    }

    public securityQuestionsDTO convertirPreguntasDTO(securityQuestionsEntity seguridaQ){
        securityQuestionsDTO dto = new securityQuestionsDTO();
        dto.setId(seguridaQ.getId());
        dto.setUniversityID(seguridaQ.getUniversityID());
        dto.setQuestion(seguridaQ.getQuestion());
        return dto;
    }
}
