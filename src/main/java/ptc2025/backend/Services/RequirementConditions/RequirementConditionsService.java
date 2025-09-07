package ptc2025.backend.Services.RequirementConditions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.RequirementConditions.RequirementConditionsEntity;
import ptc2025.backend.Entities.Requirements.RequirementsEntity;
import ptc2025.backend.Entities.SubjectDefinitions.SubjectDefinitionsEntity;
import ptc2025.backend.Exceptions.ExceptionBadRequest;
import ptc2025.backend.Exceptions.ExceptionNoSuchElement;
import ptc2025.backend.Models.DTO.RequirementConditions.RequirementConditionsDTO;
import ptc2025.backend.Respositories.RequirementConditions.RequirementConditionsRepository;
import ptc2025.backend.Respositories.Requirements.RequirementsRepository;
import ptc2025.backend.Respositories.SubjectDefinitions.SubjectDefinitionsRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RequirementConditionsService {

    @Autowired
    RequirementConditionsRepository repo;

    @Autowired
    RequirementsRepository repoRequirement;

    @Autowired
    SubjectDefinitionsRepository repoSubjectDefinitions;

    // Convertir a DTO
    public RequirementConditionsDTO convertToRequirementDTO(RequirementConditionsEntity entity){
        RequirementConditionsDTO dto = new RequirementConditionsDTO();
        dto.setConditionID(entity.getConditionID());

        if(entity.getRequirements() != null){
            dto.setRequirementID(entity.getRequirements().getId());
        } else {
            dto.setRequirementID(null);
        }

        if(entity.getSubjectDefinitions() != null){
            dto.setConditionID(entity.getSubjectDefinitions().getSubjectID());
        } else {
            dto.setSubjectID(null);
        }
        return dto;
    }

    // Convertir a Entity
    public RequirementConditionsEntity convertToRequirementEntity(RequirementConditionsDTO dto){
        RequirementConditionsEntity entity = new RequirementConditionsEntity();
        entity.setConditionID(dto.getConditionID());

        if(dto.getRequirementID() != null){
            RequirementsEntity requirements = repoRequirement.findById(dto.getRequirementID())
                    .orElseThrow(() -> new ExceptionNoSuchElement("Requisito no encontrado con ID " + dto.getRequirementID()));
            entity.setRequirements(requirements);
        }

        if(dto.getSubjectID() != null){
            SubjectDefinitionsEntity subjectDefinitions = repoSubjectDefinitions.findById(dto.getSubjectID())
                    .orElseThrow(() -> new ExceptionNoSuchElement("Materia no encontrada con ID " + dto.getSubjectID()));
            entity.setSubjectDefinitions(subjectDefinitions);
        }
        return entity;
    }

    // Obtener todos
    public List<RequirementConditionsDTO> getAllRequirements(){
        List<RequirementConditionsEntity> requirementsConditions = repo.findAll();
        return requirementsConditions.stream()
                .map(this::convertToRequirementDTO)
                .collect(Collectors.toList());
    }

    // Paginación
    public Page<RequirementConditionsDTO> getRequirementConditionPagination(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<RequirementConditionsEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertToRequirementDTO);
    }

    // Insertar
    public RequirementConditionsDTO insertRequirementCondition(RequirementConditionsDTO dto){
        if(dto == null || dto.getRequirementID() == null || dto.getRequirementID().isBlank() ||
                dto.getSubjectID() == null || dto.getSubjectID().isBlank()){
            throw new ExceptionBadRequest("Los campos deben de estar completos.");
        }
        try {
            RequirementConditionsEntity entity = convertToRequirementEntity(dto);
            RequirementConditionsEntity saved = repo.save(entity);
            return convertToRequirementDTO(saved);
        } catch (Exception e) {
            log.error("Error al registrar el requisito: " + e.getMessage());
            throw new RuntimeException("Error interno al registrar el requisito");
        }
    }

    // Actualizar
    public RequirementConditionsDTO updateRequirementCondition(String id, RequirementConditionsDTO json){
        RequirementConditionsEntity exists = repo.findById(id)
                .orElseThrow(() -> new ExceptionNoSuchElement("Requisito no encontrado con ID " + id));

        if(json.getSubjectID() != null){
            SubjectDefinitionsEntity subjectDefinitions = repoSubjectDefinitions.findById(json.getSubjectID())
                    .orElseThrow(() -> new ExceptionNoSuchElement("Materia no encontrada con ID " + json.getSubjectID()));
            exists.setSubjectDefinitions(subjectDefinitions);
        } else {
            exists.setSubjectDefinitions(null);
        }

        if(json.getRequirementID() != null){
            RequirementsEntity requirements = repoRequirement.findById(json.getRequirementID())
                    .orElseThrow(() -> new ExceptionNoSuchElement("Requisito no encontrado con ID " + json.getRequirementID()));
            exists.setRequirements(requirements);
        } else {
            exists.setRequirements(null);
        }

        RequirementConditionsEntity updatedRequirement = repo.save(exists);
        return convertToRequirementDTO(updatedRequirement);
    }

    // Eliminar
    public boolean deleteRequirementCondition(String id){
        try {
            RequirementConditionsEntity exists = repo.findById(id).orElse(null);
            if(exists != null){
                repo.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionNoSuchElement("No se encontró requisito con ID " + id + " para eliminar");
        }
    }
}
