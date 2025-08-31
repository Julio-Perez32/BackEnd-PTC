package ptc2025.backend.Services.EvaluationValidations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.EvaluationValidations.EvaluationValidationsEntity;
import ptc2025.backend.Entities.StudentEvaluations.StudentEvaluationsEntity;
import ptc2025.backend.Models.DTO.EvaluationValidations.EvaluationValidationsDTO;
import ptc2025.backend.Respositories.EvaluationValidations.EvaluationValidationsRepository;
import ptc2025.backend.Respositories.StudentEvaluations.StudentEvaluationsRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EvaluationValidationsService {

    @Autowired
    EvaluationValidationsRepository repo;

    @Autowired
    StudentEvaluationsRepository studentEvaluationsRepo;

    public List<EvaluationValidationsDTO> getAllValidations(){
        List<EvaluationValidationsEntity> validations = repo.findAll();
        return validations.stream()
                .map(this::convertToEvaluationValidationsDTO)
                .collect(Collectors.toList());
    }

    private EvaluationValidationsDTO convertToEvaluationValidationsDTO(EvaluationValidationsEntity entity) {
        EvaluationValidationsDTO dto = new EvaluationValidationsDTO();
        dto.setValidationID(entity.getValidationID());
        dto.setStatus(entity.getStatus());
        dto.setReviewedAt(entity.getReviewedAt());
        dto.setComments(entity.getComments());

        if(entity.getStudentEvaluationID() != null){
            dto.setUserFirstName(entity.getStudentEvaluationID().getUserID().getPeople().getFirstName());
            dto.setUserLastName(entity.getStudentEvaluationID().getUserID().getPeople().getLastName());
            dto.setStudentEvaluationID(entity.getStudentEvaluationID().getId());
        }else{
            dto.setUserFirstName("Nombre no asignado");
            dto.setUserFirstName("Apellido no asignado");
            dto.setStudentEvaluationID(null);
        }

        if(entity.getCreatedBy() != null){
            dto.setUserFirstName(entity.getStudentEvaluationID().getUserID().getPeople().getFirstName());
            dto.setUserLastName(entity.getStudentEvaluationID().getUserID().getPeople().getLastName());
            dto.setCreatedBy(entity.getStudentEvaluationID().getUserID().getId());
        }else{
            dto.setUserFirstName("Nombre no asignado");
            dto.setUserFirstName("Apellido no asignado");
            dto.setCreatedBy(null);
        }

        if(entity.getReviewedBy() != null){
            dto.setUserFirstName(entity.getStudentEvaluationID().getUserID().getPeople().getFirstName());
            dto.setUserLastName(entity.getStudentEvaluationID().getUserID().getPeople().getLastName());
            dto.setReviewedBy(entity.getStudentEvaluationID().getUserID().getId());
        }else{
            dto.setUserFirstName("Nombre no asignado");
            dto.setUserFirstName("Apellido no asignado");
            dto.setReviewedBy(null);
        }

        return dto;
    }

    private EvaluationValidationsEntity convertToEvaluationValidationsEntity(EvaluationValidationsDTO dto){
        EvaluationValidationsEntity entity = new EvaluationValidationsEntity();
        entity.setValidationID(dto.getValidationID());
        entity.setStatus(dto.getStatus());
        entity.setReviewedAt(dto.getReviewedAt());
        entity.setComments(dto.getComments());

        if(dto.getStudentEvaluationID() != null){
            StudentEvaluationsEntity studentEvaluationID = studentEvaluationsRepo.findById(dto.getStudentEvaluationID())
                    .orElseThrow(() -> new IllegalArgumentException("Evaluación de estudiante no encontrada con ID " + dto.getStudentEvaluationID()));
            entity.setStudentEvaluationID(studentEvaluationID);
        }

        if(dto.getCreatedBy() != null){
            StudentEvaluationsEntity createdBy = studentEvaluationsRepo.findById(dto.getCreatedBy())
                    .orElseThrow(() -> new IllegalArgumentException("Evaluación de estudiante no encontrada con ID " + dto.getCreatedBy()));
            entity.setCreatedBy(createdBy);
        }

        if(dto.getReviewedBy() != null){
            StudentEvaluationsEntity reviewedBy = studentEvaluationsRepo.findById(dto.getReviewedBy())
                    .orElseThrow(() -> new IllegalArgumentException("Evaluación de estudiante no encontrada con ID " + dto.getReviewedBy()));
            entity.setReviewedBy(reviewedBy);
        }

        return entity;
    }

    public EvaluationValidationsDTO insertEvaluationValidation(EvaluationValidationsDTO dto){
        if(dto == null || dto.getStatus() == null || dto.getStatus().isBlank()){
            throw new IllegalArgumentException("");
        }
        try{
            EvaluationValidationsEntity entity = convertToEvaluationValidationsEntity(dto);
            EvaluationValidationsEntity saved = repo.save(entity);
            return convertToEvaluationValidationsDTO(saved);
        }catch (Exception e){
            log.error("Error al registrar la validación de la evaluación " + e.getMessage());
            throw new IllegalArgumentException("Error interno al registrar la validación de la evaluación");
        }
    }

    public EvaluationValidationsDTO updateEvaluationValidation(String id, EvaluationValidationsDTO json){
        EvaluationValidationsEntity exists = repo.findById(id).orElseThrow(() -> new IllegalArgumentException(""));
        exists.setStatus(json.getStatus());
        exists.setReviewedAt(json.getReviewedAt());
        exists.setComments(json.getComments());

        if(json.getStudentEvaluationID() != null){
            StudentEvaluationsEntity studentEvaluations = studentEvaluationsRepo.findById(json.getStudentEvaluationID())
                    .orElseThrow(() -> new IllegalArgumentException("Evaluación no encontrada con ID " + json.getStudentEvaluationID()));
            exists.setStudentEvaluationID(studentEvaluations);
        }

        if(json.getCreatedBy() != null){
            StudentEvaluationsEntity createdBy = studentEvaluationsRepo.findById(json.getCreatedBy())
                    .orElseThrow(() -> new IllegalArgumentException("Creador no encontrado con ID " + json.getCreatedBy()));
            exists.setCreatedBy(createdBy);
        }

        if(json.getReviewedBy() != null){
            StudentEvaluationsEntity reviewedBy = studentEvaluationsRepo.findById(json.getReviewedBy())
                    .orElseThrow(() -> new IllegalArgumentException("Revisador no encontrado con ID " + json.getReviewedBy()));
            exists.setReviewedBy(reviewedBy);
        }

        EvaluationValidationsEntity updated = repo.save(exists);

        return convertToEvaluationValidationsDTO(updated);
    }

    public boolean deleteEvaluationValidation(String id){
        try{
            EvaluationValidationsEntity exists = repo.findById(id).orElse(null);

            if (exists != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se pudo encontrar validación de evaluación con ID " + id + e.getMessage(),1);
        }
    }
}
