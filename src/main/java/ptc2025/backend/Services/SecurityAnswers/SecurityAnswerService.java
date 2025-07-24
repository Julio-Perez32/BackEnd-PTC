package ptc2025.backend.Services.SecurityAnswers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.SecurityAnswers.SecurityAnswersEntity;
import ptc2025.backend.Models.DTO.SecurityAnswers.SecurityAnswersDTO;
import ptc2025.backend.Respositories.SecurityAnswers.SecurityAnswerRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SecurityAnswerService {
    @Autowired
    private SecurityAnswerRepository repo;

    public List<SecurityAnswersDTO> getSecurityAnswers() {
        List<SecurityAnswersEntity> Lista = repo.findAll();
        return Lista.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public SecurityAnswersDTO convertirADTO(SecurityAnswersEntity securityAnswers){
        SecurityAnswersDTO dto = new SecurityAnswersDTO();
        dto.setId(securityAnswers.getId());
        dto.setQuestionID(securityAnswers.getQuestionID());
        dto.setUserID(securityAnswers.getUserID());
        dto.setAnswer(securityAnswers.getAnswer());
        return dto;
    }
}
