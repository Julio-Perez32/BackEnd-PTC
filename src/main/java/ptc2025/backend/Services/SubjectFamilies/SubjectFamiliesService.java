package ptc2025.backend.Services.SubjectFamilies;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.SubjectFamilies.SubjectFamiliesEntity;
import ptc2025.backend.Models.DTO.SubjectFamilies.SubjectFamiliesDTO;
import ptc2025.backend.Respositories.Faculties.FacultiesRepository;
import ptc2025.backend.Respositories.SubjectFamilies.SubjectFamiliesRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SubjectFamiliesService {

    @Autowired
    SubjectFamiliesRepository repo;

    @Autowired
    FacultiesRepository repoFaculties;

    public List<SubjectFamiliesDTO> getSubjectFam(){
        List<SubjectFamiliesEntity> families = repo.findAll();
        return families.stream()
                .map(this::convertToSubjectFamDTO)
                .collect(Collectors.toList());
    }

    private SubjectFamiliesDTO convertToSubjectFamDTO(SubjectFamiliesEntity families) {
        SubjectFamiliesDTO dto = new SubjectFamiliesDTO();
        dto.setSubjectFamilyID(families.getSubjectFamilyID());
        dto.setSubjectPrefix(families.getSubjectPrefix());
        dto.setReservedSlots(families.getReservedSlots());
        dto.setStartingNumber(families.getStartingNumber());
        dto.setLastAssignedNumber(families.getLastAssignedNumber());

        if(families.getFaculty() != null){
            dto.setFacultyName(families.getFaculty().getFacultyName());
            dto.setFacultyID(families.getFaculty().getFacultyID());
        }else{
            dto.setFacultyName("Sin facultad asignada");
            dto.setFacultyID(null);
        }
        return dto;
    }

    private SubjectFamiliesEntity convertToSubjectFamEntity(SubjectFamiliesDTO dto){
        SubjectFamiliesEntity entity = new SubjectFamiliesEntity();
        entity.setSubjectFamilyID(dto.getSubjectFamilyID());
        entity.setFacultyID(dto.getFacultyID());
        entity.setSubjectPrefix(dto.getSubjectPrefix());
        entity.setReservedSlots(dto.getReservedSlots());
        entity.setStartingNumber(dto.getStartingNumber());
        entity.setLastAssignedNumber(dto.getLastAssignedNumber());
        return entity;
    }

    public SubjectFamiliesDTO insertSubjectFam(SubjectFamiliesDTO dto){
        if (dto == null || dto.getFacultyID() == null || dto.getFacultyID().isEmpty() || dto.getSubjectPrefix() == null ||
                dto.getSubjectPrefix().isEmpty() || dto.getReservedSlots() == null || dto.getStartingNumber() == null ||
                dto.getLastAssignedNumber() == null){
            throw new IllegalArgumentException("Todos los campos deben de ser llenados.");
        }
        try{
            SubjectFamiliesEntity entity = convertToSubjectFamEntity(dto);
            SubjectFamiliesEntity familySaved = repo.save(entity);
            return convertToSubjectFamDTO(familySaved);
        }catch (Exception e){
            log.error("Error al registrar las materias: " + e.getMessage());
            throw new IllegalArgumentException("Error al registrar las materias.");
        }
    }

    public SubjectFamiliesDTO updateSubjectFam(String ID, SubjectFamiliesDTO json){
        SubjectFamiliesEntity existsSubjectFam = repo.findById(ID).orElseThrow(() -> new IllegalArgumentException("Familia de Materias no encontrada."));
        existsSubjectFam.setFacultyID(json.getFacultyID());
        existsSubjectFam.setSubjectPrefix(json.getSubjectPrefix());
        existsSubjectFam.setReservedSlots(json.getReservedSlots());
        existsSubjectFam.setLastAssignedNumber(json.getLastAssignedNumber());

        SubjectFamiliesEntity updatedSubjectFam = repo.save(existsSubjectFam);

        return convertToSubjectFamDTO(updatedSubjectFam);
    }

    public boolean deleteSubjectFam(String ID){
        try{
            SubjectFamiliesEntity existsSubjectFam = repo.findById(ID).orElse(null);
            if (existsSubjectFam != null){
                repo.deleteById(ID);
                return true;
            }else{
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("No se encontró familia de materias con ID: " + ID + " para eliminar.",1);
        }
    }
}
