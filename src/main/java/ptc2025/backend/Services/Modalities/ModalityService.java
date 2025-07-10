package ptc2025.backend.Services.Modalities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.Modalities.ModalitiesEntity;
import ptc2025.backend.Models.DTO.Modalities.ModalitiesDTO;
import ptc2025.backend.Respositories.Modalities.ModalityRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModalityService {

    @Autowired
    private ModalityRepository repo;

    public List<ModalitiesDTO> getAllModalities(){
        List<ModalitiesEntity> modalities = repo.findAll();
        return modalities.stream()
                .map(this::convertirModalidadesADTO)
                .collect(Collectors.toList());
    }

    public ModalitiesDTO convertirModalidadesADTO(ModalitiesEntity modalida){
        ModalitiesDTO dto = new ModalitiesDTO();
        dto.setId(modalida.getId());
        dto.setUniversityID(modalida.getUniversityID());
        dto.setModalityName(modalida.getModalityName());
        return dto;
    }
}
