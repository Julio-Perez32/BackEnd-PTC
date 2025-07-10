package ptc2025.backend.Services.degreeTypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.degreeTypes.DegreeTypesEntity;
import ptc2025.backend.Models.DTO.degreeTypes.DegreeTypesDTO;
import ptc2025.backend.Respositories.degreeTypes.DegreeTypeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DegreeTypeService {

    @Autowired
    private DegreeTypeRepository repo;

    public List<DegreeTypesDTO> getAllDegreeTypes(){
        List<DegreeTypesEntity> degreetype = repo.findAll();
        return degreetype.stream()
                .map(this::hacerdegreestypeADTO)
                .collect(Collectors.toList());
    }

    public DegreeTypesDTO hacerdegreestypeADTO(DegreeTypesEntity degreeT) {

        DegreeTypesDTO dto = new DegreeTypesDTO();
        dto.setId(degreeT.getId());
        dto.setUniversityID(degreeT.getUniversityID());
        dto.setDegreeTypeName(degreeT.getDegreeTypeName());
        return dto;
    }
}
