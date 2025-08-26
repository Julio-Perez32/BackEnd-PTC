package ptc2025.backend.Services.RequirementConditions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.Pensum.PensumEntity;
import ptc2025.backend.Entities.RequirementConditions.RequirementConditionsEntity;
import ptc2025.backend.Models.DTO.Pensum.PensumDTO;
import ptc2025.backend.Models.DTO.RequirementConditions.RequirementConditionsDTO;
import ptc2025.backend.Respositories.RequirementConditions.RequirementConditionsRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RequirementConditionsService {

    @Autowired
    RequirementConditionsRepository repo;

    public RequirementConditionsDTO convertToRequirementDTO(RequirementConditionsEntity entity){
        RequirementConditionsDTO dto = new RequirementConditionsDTO();
        dto.setConditionID(entity.getConditionID());

        if(entity.getRequirements() != null){
            dto.setRequirementID(entity.getRequirements().getId());
        }else {
            dto.setRequirementID(null);
        }

        if(entity.getSubjectDefinitions() != null){
            dto.setConditionID(entity.getSubjectDefinitions().getSubjectID());
        }else {
            dto.setSubjectID(null);
        }
        return dto;
    }

    public RequirementConditionsEntity convertToRequirementEntity(RequirementConditionsDTO dto){
        RequirementConditionsEntity entity = new RequirementConditionsEntity();
        entity.setConditionID(dto.getConditionID());
        entity.setRequirementID(dto.getRequirementID());
        entity.setSubjectID(dto.getSubjectID());
        return entity;
    }

    public List<RequirementConditionsDTO> getAllRequirements(){
        List<RequirementConditionsEntity> pensa = repo.findAll();
        return pensa.stream()
                .map(this::convertToRequirementDTO)
                .collect(Collectors.toList());
    }

    public RequirementConditionsDTO insertRequirementCondition(RequirementConditionsDTO dto){
        if(dto == null || dto.getRequirementID() == null || dto.getRequirementID().isBlank() ||
                dto.getSubjectID() == null || dto.getSubjectID().isBlank()){
            throw new IllegalArgumentException("Los campos deben de estar completos.");
        }
        try{
            RequirementConditionsEntity entity = convertToRequirementEntity(dto);
            RequirementConditionsEntity saved = repo.save(entity);
            return convertToRequirementDTO(saved);
        }catch (Exception e){
            log.error("Error al registrar el requisito: " + e.getMessage());
            throw new IllegalArgumentException("Error al registrar el requisito");
        }
    }

    public RequirementConditionsDTO updateRequirementCondition (String id, RequirementConditionsDTO json){
        RequirementConditionsEntity exists = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Requisito no encontrado"));
        exists.setRequirementID(json.getRequirementID());
        exists.setSubjectID(json.getSubjectID());

        RequirementConditionsEntity updatedRequirement = repo.save(exists);

        return convertToRequirementDTO(updatedRequirement);
    }

    public boolean deleteRequirementCondition(String id){
        try{
            RequirementConditionsEntity exists = repo.findById(id).orElse(null);
            if(exists != null){
                repo.deleteById(id);
                return true;
            }else{
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontró requisito con ID " + id + " para eliminar",1);
        }
    }
}
