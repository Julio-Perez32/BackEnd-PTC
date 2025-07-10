package ptc2025.backend.Services.Requirements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.Requirements.RequirementsEntity;
import ptc2025.backend.Models.DTO.Requirements.RequirementsDTO;
import ptc2025.backend.Respositories.Requirements.RequirementRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequirementService {

    @Autowired
    private RequirementRepository repo;

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

}
