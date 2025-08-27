package ptc2025.backend.Services.SubjectDefinitions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.Pensum.PensumEntity;
import ptc2025.backend.Entities.SubjectDefinitions.SubjectDefinitionsEntity;
import ptc2025.backend.Entities.SubjectFamilies.SubjectFamiliesEntity;
import ptc2025.backend.Models.DTO.Pensum.PensumDTO;
import ptc2025.backend.Models.DTO.SubjectDefinitions.SubjectDefinitionsDTO;
import ptc2025.backend.Respositories.SubjectDefinitions.SubjectDefinitionsRepository;
import ptc2025.backend.Respositories.SubjectFamilies.SubjectFamiliesRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SubjectDefinitionsService {

    @Autowired
    SubjectDefinitionsRepository repo;

    @Autowired
    SubjectFamiliesRepository repoSubjectFamilies;

    @Autowired
    SubjectDefinitionsRepository repoSubjectDefinitions;

    public SubjectDefinitionsDTO convertToDefinitionDTO(SubjectDefinitionsEntity entity){
        SubjectDefinitionsDTO dto = new SubjectDefinitionsDTO();
        dto.setSubjectID(entity.getSubjectID());
        dto.setSubjectName(entity.getSubjectName());
        dto.setSubjectCode(entity.getSubjectCode());

        if(entity.getSubjectFamilies() != null){
            dto.setSubjectFamilyID(entity.getSubjectFamilies().getSubjectFamilyID());
        }else {
            dto.setSubjectName("Sin Materia asignada");
            dto.setSubjectFamilyID(null);
        }
        return dto;
    }

    public SubjectDefinitionsEntity convertToDefinitionEntity(SubjectDefinitionsDTO dto){
        SubjectDefinitionsEntity entity = new SubjectDefinitionsEntity();
        entity.setSubjectName(dto.getSubjectName());
        entity.setSubjectCode(dto.getSubjectCode());

        if(dto.getSubjectFamilyID() != null){
            SubjectFamiliesEntity families = repoSubjectFamilies.findById(dto.getSubjectFamilyID()).orElseThrow(
                    () -> new IllegalArgumentException("Familia de Materias no encontradas con ID" + dto.getSubjectFamilyID()));
            entity.setSubjectFamilies(families);
        }

        if (dto.getSubjectID() != null) {
            SubjectDefinitionsEntity subjectDefinitions = repoSubjectDefinitions.findById(dto.getSubjectID()).orElseThrow(
                    () -> new IllegalArgumentException("Materia no encontrada con ID " + dto.getSubjectID()));
        }

        return entity;
    }

    public List<SubjectDefinitionsDTO> getAllSubjectDefinitions(){
        List<SubjectDefinitionsEntity> pensa = repo.findAll();
        return pensa.stream()
                .map(this::convertToDefinitionDTO)
                .collect(Collectors.toList());
    }

    public SubjectDefinitionsDTO insertDefintion(SubjectDefinitionsDTO dto){
        if(dto == null || dto.getSubjectFamilyID() == null || dto.getSubjectFamilyID().isBlank() || dto.getSubjectName() == null ||
                dto.getSubjectName().isBlank() || dto.getSubjectCode() == null || dto.getSubjectCode().isBlank()){
            throw new IllegalArgumentException("Todos los campos deben de estar completos");
        }
        try{
            SubjectDefinitionsEntity entity = convertToDefinitionEntity(dto);
            SubjectDefinitionsEntity saved = repo.save(entity);
            return convertToDefinitionDTO(saved);
        }catch (Exception e){
            log.error("Error al registrar la definición de la materia: " + e.getMessage());
            throw new IllegalArgumentException("");
        }
    }

    public SubjectDefinitionsDTO updateDefinition(String id,SubjectDefinitionsDTO json){
        SubjectDefinitionsEntity exists = repo.findById(id).orElseThrow(() -> new IllegalArgumentException(""));
        exists.setSubjectName(json.getSubjectName());
        exists.setSubjectCode(json.getSubjectCode());

        if (json.getSubjectID() != null) {
            SubjectDefinitionsEntity subjectDefinitions = repoSubjectDefinitions.findById(json.getSubjectID()).orElseThrow(
                    () -> new IllegalArgumentException("Materia no encontrada con ID " + json.getSubjectID()));
        }

        if (json.getSubjectFamilyID() != null) {
            SubjectFamiliesEntity subjectFamilies = repoSubjectFamilies.findById(json.getSubjectFamilyID()).orElseThrow(
                    () -> new IllegalArgumentException("Familia de Materia no encontrada con ID " + json.getSubjectFamilyID()));
        }

        SubjectDefinitionsEntity updated = repo.save(exists);

        return convertToDefinitionDTO(updated);
    }

    public boolean deleteDefinition(String id){
        try{
            SubjectDefinitionsEntity exists = repo.findById(id).orElse(null);
            if(exists != null){
                repo.deleteById(id);
                return true;
            }else{
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontró definición con ID " + id + " para eliminar",1);
        }
    }
}
