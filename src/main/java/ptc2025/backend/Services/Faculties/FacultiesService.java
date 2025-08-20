package ptc2025.backend.Services.Faculties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.Faculties.FacultiesEntity;
import ptc2025.backend.Models.DTO.Faculties.FacultiesDTO;
import ptc2025.backend.Respositories.Faculties.FacultiesRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FacultiesService {

    @Autowired
    FacultiesRepository repo;

    public List<FacultiesDTO> getAllFaculties(){
        List<FacultiesEntity> faculties = repo.findAll();
        return faculties.stream()
                .map(this::convertToFacultiesDTO)
                .collect(Collectors.toList());
    }

    public FacultiesDTO convertToFacultiesDTO(FacultiesEntity faculties){
        FacultiesDTO dto = new FacultiesDTO();
        dto.setFacultyID(faculties.getFacultyID());
        dto.setFacultyCode(faculties.getFacultyCode());
        dto.setContactPhone(faculties.getContactPhone());
        dto.setCorrelativeCode(faculties.getCorrelativeCode());
        return dto;
    }

    public FacultiesEntity convertToFacultiesEntity(FacultiesDTO dto){
        FacultiesEntity entity = new FacultiesEntity();
        entity.setFacultyID(dto.getFacultyID());
        entity.setFacultyCode(dto.getFacultyCode());
        entity.setContactPhone(dto.getContactPhone());
        entity.setCorrelativeCode(dto.getCorrelativeCode());
        return entity;
    }

    public FacultiesDTO insertFaculty(FacultiesDTO dto){
        if (dto == null || dto.getFacultyCode() == null || dto.getFacultyCode().isEmpty() || dto.getContactPhone() == null
                || dto.getContactPhone().isEmpty() || dto.getCorrelativeCode() == null || dto.getCorrelativeCode().isEmpty()){
            throw new IllegalArgumentException("Faculty needs to have all of the fields completed");
        }
        try{
            FacultiesEntity entity = convertToFacultiesEntity(dto);
            FacultiesEntity facultySaved = repo.save(entity);
            return convertToFacultiesDTO(facultySaved);
        }catch (Exception e){
            log.error("Error al registrar la facultad: " + e.getMessage());
            throw new IllegalArgumentException("Error al registrar la facultad.");
        }
    }

    public FacultiesDTO updateFaculty(String id, FacultiesDTO json){
        FacultiesEntity existsFaculty = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Facultad no encontrada"));
        existsFaculty.setFacultyCode(json.getFacultyCode());
        existsFaculty.setContactPhone(json.getContactPhone());
        existsFaculty.setCorrelativeCode(json.getCorrelativeCode());

        FacultiesEntity updatedFaculty = repo.save(existsFaculty);

        return convertToFacultiesDTO(updatedFaculty);
    }

    public boolean deleteFaculty(String id){
        try{
            FacultiesEntity facultyExists = repo.findById(id).orElse(null);
            if(facultyExists != null){
                repo.deleteById(id);
                return true;
            }else{
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontró Facultad con ID: " + id + " para eliminar",1);
        }
    }
}
