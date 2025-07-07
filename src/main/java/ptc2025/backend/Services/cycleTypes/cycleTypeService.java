package ptc2025.backend.Services.cycleTypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.cycleTypes.cycleTypesEntity;
import ptc2025.backend.Models.DTO.cycleTypes.cycleTypesDTO;
import ptc2025.backend.Respositories.cycleTypes.cycleTypeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class cycleTypeService {

    @Autowired
    private cycleTypeRepository repo;

    public List<cycleTypesDTO> getAllCycleTypes(){
        List<cycleTypesEntity> cycletype = repo.findAll();
        return cycletype.stream()
                .map(this::ConvertCycleTypeDTO)
                .collect(Collectors.toList());
    }

    public cycleTypesDTO ConvertCycleTypeDTO(cycleTypesEntity cycleType){
        cycleTypesDTO dto = new cycleTypesDTO();
        dto.setId(cycleType.getId());
        dto.setUniversityID(cycleType.getUniversityID());
        dto.setCycleLabel(cycleType.getCycleLabel());
        return dto;
    }

}
