package ptc2025.backend.Services.Requirements;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.Requirements.RequirementsEntity;
import ptc2025.backend.Models.DTO.Requirements.RequirementsDTO;
import ptc2025.backend.Respositories.Requirements.RequirementsRepository;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class RequirementsService {

    @Autowired
    private RequirementsRepository repo;

    public List<RequirementsDTO> getAllRequirements(){
        List<RequirementsEntity> requirement = repo.findAll();
        return requirement.stream()
                .map(this::convertirRequerimientosADTO)
                .collect(Collectors.toList());
    }
    public RequirementsDTO convertirRequerimientosADTO(RequirementsEntity reque){
        RequirementsDTO dto = new RequirementsDTO();
        dto.setId(reque.getId());
        dto.setUniversityID(reque.getUniversityID());
        dto.setRequirementName(reque.getRequirementName());
        dto.setDescription(reque.getDescription());
        return dto;
    }

    public RequirementsDTO insertarDatos(RequirementsDTO data) {
        if (data == null){
            throw new IllegalArgumentException("Los datos no pueden ser nulos");
        }
        try {
            RequirementsEntity entity = converirAEntity(data);
            RequirementsEntity RegistroGuardado = repo.save(entity);
            return convertirRequerimientosADTO(RegistroGuardado);
        }catch (Exception e){
           log.error("Error al ingresar los datos" + e.getMessage());
           throw new RuntimeException("Error al registrar El nuevo Requirement");
        }
    }

    private RequirementsEntity converirAEntity(RequirementsDTO data) {
        RequirementsEntity entity = new RequirementsEntity();
        entity.setUniversityID(data.getUniversityID());
        entity.setRequirementName(data.getRequirementName());
        entity.setDescription(data.getDescription());
        return entity;
    }

    public RequirementsDTO actualizarRequirements(String id, RequirementsDTO json) {
        RequirementsEntity existente = repo.findById(id).orElseThrow(() -> new RuntimeException("Requirement no encontrado"));
        existente.setUniversityID(json.getUniversityID());
        existente.setRequirementName(json.getRequirementName());
        existente.setDescription(json.getDescription());
        RequirementsEntity RequirementActualizado = repo.save(existente);
        return convertirRequerimientosADTO(RequirementActualizado);
    }

    public boolean eliminarRequirement(String id) {
        try{
            RequirementsEntity existente = repo.findById(id).orElse(null);
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
