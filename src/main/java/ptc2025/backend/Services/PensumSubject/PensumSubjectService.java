package ptc2025.backend.Services.PensumSubject;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.Pensum.PensumEntity;
import ptc2025.backend.Entities.PensumSubject.PensumSubjectEntity;
import ptc2025.backend.Models.DTO.Pensum.PensumDTO;
import ptc2025.backend.Models.DTO.PensumSubject.PensumSubjectDTO;
import ptc2025.backend.Respositories.Pensum.PensumRepository;
import ptc2025.backend.Respositories.PensumSubject.PensumSubjectRepository;
import ptc2025.backend.Respositories.SubjectDefinitions.SubjectDefinitionsRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PensumSubjectService {

    @Autowired
    PensumSubjectRepository repo;

    @Autowired
    PensumRepository repoPensum;

    @Autowired
    SubjectDefinitionsRepository repoSubjectDefinitions;

    public PensumSubjectDTO convertToPensumSubjectDTO(PensumSubjectEntity entity){
        PensumSubjectDTO dto = new PensumSubjectDTO();
        dto.setPensumSubjectID(entity.getPensumSubjectID());
        dto.setValueUnits(entity.getValueUnits());
        dto.setIsRequired(entity.getIsRequired());

        if(entity.getPensum() != null){
            dto.setPensumID(entity.getPensum().getPensumID());
        }else {
            dto.setPensumID(null);
        }

        if(entity.getSubjectDefinitions() != null){
            dto.setSubjectID(entity.getSubjectDefinitions().getSubjectID());
        }else{
            dto.setSubjectID(null);
        }

        return dto;
    }

    public PensumSubjectEntity convertToPensumSubjectEntity(PensumSubjectDTO dto){
        PensumSubjectEntity entity = new PensumSubjectEntity();
        entity.setPensumSubjectID(dto.getPensumSubjectID());
        entity.setValueUnits(dto.getValueUnits());
        entity.setIsRequired(dto.getIsRequired());

        if(dto.getPensumID() != null){
            PensumEntity pensum = repoPensum.findById(dto.getPensumID()).orElseThrow(
                    () -> new IllegalArgumentException("Pensum no encontrado con ID " + dto.getPensumID()));
            entity.setPensum(pensum);
        }
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
        exists.setValueUnits(json.getValueUnits());
        exists.setIsRequired(json.getIsRequired());

        if(json.getPensumID() != null){
            PensumEntity pensum = repoPensum.findById(json.getPensumID()).orElseThrow(
                    () -> new IllegalArgumentException("Pensum no encontrado con ID " + json.getPensumID()));
            exists.setPensum(pensum);
        }else {
            exists.setPensum(null);
        }

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
