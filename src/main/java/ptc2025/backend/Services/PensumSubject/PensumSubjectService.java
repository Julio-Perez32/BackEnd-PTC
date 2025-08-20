package ptc2025.backend.Services.PensumSubject;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.Pensum.PensumEntity;
import ptc2025.backend.Entities.PensumSubject.PensumSubjectEntity;
import ptc2025.backend.Models.DTO.Pensum.PensumDTO;
import ptc2025.backend.Models.DTO.PensumSubject.PensumSubjectDTO;
import ptc2025.backend.Respositories.PensumSubject.PensumSubjectRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PensumSubjectService {

    @Autowired
    PensumSubjectRepository repo;

    public PensumSubjectDTO convertToPensumSubjectDTO(PensumSubjectEntity entity){
        PensumSubjectDTO dto = new PensumSubjectDTO();
        dto.setPensumSubjectID(entity.getPensumSubjectID());
        dto.setPensumID(entity.getPensumID());
        dto.setSubjectID(entity.getSubjectID());
        dto.setValueUnits(entity.getValueUnits());
        dto.setIsRequired(entity.getIsRequired());
        return dto;
    }

    public PensumSubjectEntity convertToPensumSubjectEntity(PensumSubjectDTO dto){
        PensumSubjectEntity entity = new PensumSubjectEntity();
        entity.setPensumSubjectID(dto.getPensumSubjectID());
        entity.setPensumID(dto.getPensumID());
        entity.setSubjectID(dto.getSubjectID());
        entity.setValueUnits(dto.getValueUnits());
        entity.setIsRequired(dto.getIsRequired());
        return entity;
    }

    public List<PensumSubjectDTO> getPensumSubjects(){
        List<PensumSubjectEntity> pensa = repo.findAll();
        return pensa.stream()
                .map(this::convertToPensumSubjectDTO)
                .collect(Collectors.toList());
    }

    public PensumSubjectDTO insertPensumSubject(PensumSubjectDTO dto){
        if(dto == null || dto.getPensumID() == null || dto.getPensumID().isBlank() || dto.getSubjectID() == null
                || dto.getSubjectID().isBlank() || dto.getValueUnits() == null || dto.getIsRequired() == null){
            throw new IllegalArgumentException("Los campos deben de estar completos");
        }
        try{
            PensumSubjectEntity entity = convertToPensumSubjectEntity(dto);
            PensumSubjectEntity saved = repo.save(entity);
            return convertToPensumSubjectDTO(saved);
        }catch (Exception e){
            log.error("Error al registrar la materia para el pensum: " + e.getMessage());
            throw new IllegalArgumentException("Error al registrar la materia para el pensum.");
        }
    }

    public PensumSubjectDTO updatePensumSubject(String id, PensumSubjectDTO json){
        PensumSubjectEntity exists = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Materia de pensum no encontrada."));
        exists.setPensumID(json.getPensumID());
        exists.setSubjectID(json.getSubjectID());
        exists.setValueUnits(json.getValueUnits());
        exists.setIsRequired(json.getIsRequired());

        PensumSubjectEntity updatedPensumSubject = repo.save(exists);

        return convertToPensumSubjectDTO(updatedPensumSubject);
    }

    public boolean removePensumSubject(String id){
        try{
            PensumSubjectEntity exists = repo.findById(id).orElse(null);
            if(exists != null){
                repo.deleteById(id);
                return true;
            }else{
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontró materia con ID " + id + " para eliminar",1);
        }
    }
}
