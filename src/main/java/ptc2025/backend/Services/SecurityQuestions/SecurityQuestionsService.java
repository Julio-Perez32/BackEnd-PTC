package ptc2025.backend.Services.SecurityQuestions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.SecurityQuestions.SecurityQuestionsEntity;
import ptc2025.backend.Models.DTO.SecurityQuestions.SecurityQuestionsDTO;
import ptc2025.backend.Respositories.SecurityQuestions.SecurityQuestionsRepository;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class SecurityQuestionsService {

    @Autowired
    private SecurityQuestionsRepository repo;

    public List<SecurityQuestionsDTO> getAllSecurityQuestions(){
        List<SecurityQuestionsEntity> secuQ = repo.findAll();
        return secuQ.stream()
                .map(this::convertirPreguntasDTO)
                .collect(Collectors.toList());
    }

    public SecurityQuestionsDTO convertirPreguntasDTO(SecurityQuestionsEntity seguridaQ){
        SecurityQuestionsDTO dto = new SecurityQuestionsDTO();
        dto.setId(seguridaQ.getId());
        dto.setUniversityID(seguridaQ.getUniversityID());
        dto.setQuestion(seguridaQ.getQuestion());
        return dto;
    }

    public SecurityQuestionsDTO insertarDatos(SecurityQuestionsDTO data) {
        if (data == null){
            throw new IllegalArgumentException("Los valores no pueden ser nulos");
        }
        try {
            SecurityQuestionsEntity entity = convertirAEntity(data);
            SecurityQuestionsEntity securityQuestionGuardada = repo.save(entity);
            return convertirPreguntasDTO(securityQuestionGuardada);
        }catch (Exception e){
            log.error("Error al registrar el valor" + e.getMessage());
            throw new RuntimeException("Error al hacer el nuevo registro");
        }
    }

    private SecurityQuestionsEntity convertirAEntity(SecurityQuestionsDTO data) {
        SecurityQuestionsEntity entity = new SecurityQuestionsEntity();
        entity.setUniversityID(data.getUniversityID());
        entity.setQuestion(data.getQuestion());
        return entity;
    }

    public SecurityQuestionsDTO actualizarSecurityQuestion(String id, SecurityQuestionsDTO json) {
        SecurityQuestionsEntity existente = repo.findById(id).orElseThrow(() -> new RuntimeException("SecurityQuestion no encontrada"));
        existente.setUniversityID(json.getUniversityID());
        existente.setQuestion(json.getQuestion());
        SecurityQuestionsEntity securityQuestionsActualizado = repo.save(existente);
        return convertirPreguntasDTO(securityQuestionsActualizado);
    }

    public boolean eliminarSecurityQuestion(String id) {
        try {
            SecurityQuestionsEntity existente = repo.findById(id).orElse(null);
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
