package ptc2025.backend.Services.securityQuestions;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.securityQuestions.securityQuestionsEntity;
import ptc2025.backend.Models.DTO.securityQuestions.securityQuestionsDTO;
import ptc2025.backend.Respositories.securityQuestions.securityQuestionRepository;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
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

    public securityQuestionsDTO insertarDatos(securityQuestionsDTO data) {
        if (data == null){
            throw new IllegalArgumentException("Los valores no pueden ser nulos");
        }
        try {
            securityQuestionsEntity entity = convertirAEntity(data);
            securityQuestionsEntity securityQuestionGuardada = repo.save(entity);
            return convertirPreguntasDTO(securityQuestionGuardada);
        }catch (Exception e){
            log.error("Error al registrar el valor" + e.getMessage());
            throw new RuntimeException("Error al hacer el nuevo registro");
        }
    }

    private securityQuestionsEntity convertirAEntity(securityQuestionsDTO data) {
        securityQuestionsEntity entity = new securityQuestionsEntity();
        entity.setUniversityID(data.getUniversityID());
        entity.setQuestion(data.getQuestion());
        return entity;
    }

    public securityQuestionsDTO actualizarSecurityQuestion(String id, securityQuestionsDTO json) {
        securityQuestionsEntity existente = repo.findById(id).orElseThrow(() -> new RuntimeException("SecurityQuestion no encontrada"));
        existente.setUniversityID(json.getUniversityID());
        existente.setQuestion(json.getQuestion());
        securityQuestionsEntity securityQuestionsActualizado = repo.save(existente);
        return convertirPreguntasDTO(securityQuestionsActualizado);
    }

    public boolean eliminarSecurityQuestion(String id) {
        try {
            securityQuestionsEntity existente = repo.findById(id).orElse(null);
            if (existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro el registro", 1);
        }
    }
}
