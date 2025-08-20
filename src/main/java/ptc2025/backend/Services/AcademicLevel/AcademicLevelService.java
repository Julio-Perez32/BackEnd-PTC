package ptc2025.backend.Services.AcademicLevel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Execeptions.ExceptionLevelNotValid;
import ptc2025.backend.Models.DTO.AcademicLevel.AcademicLevelsDTO;
import ptc2025.backend.Respositories.AcademicLevel.AcademicLevelsRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AcademicLevelService {

    @Autowired
    AcademicLevelsRepository repo;

    public List<AcademicLevelsDTO> getAllLevels(){
        List<AcademicLevelsEntity> levels = repo.findAll();
        return levels.stream()
                .map(this::convertToLevelsDTO)
                .collect(Collectors.toList());
    }

    public AcademicLevelsDTO convertToLevelsDTO(AcademicLevelsEntity level){
        AcademicLevelsDTO dto = new AcademicLevelsDTO();
        dto.setAcademicLevelID(level.getAcademicLevelID());
        dto.setUniversityID(level.getUniversityID());
        dto.setAcademicLevelName(level.getAcademicLevelName());
        return dto;
    }

    public AcademicLevelsEntity convertToLevelEntity (AcademicLevelsDTO dto){
        AcademicLevelsEntity level = new AcademicLevelsEntity();
        level.setAcademicLevelID(dto.getAcademicLevelID());
        level.setUniversityID(dto.getUniversityID());
        level.setAcademicLevelName(dto.getAcademicLevelName());
        return level;
    }

    public AcademicLevelsDTO insertLevel(AcademicLevelsDTO dto){

        if (dto == null || dto.getUniversityID() == null || dto.getUniversityID().isBlank() || dto.getAcademicLevelName() == null || dto.getAcademicLevelName().isBlank()){
            throw new IllegalArgumentException("Todos los campos deben estar completados.");
        }
        try{
            AcademicLevelsEntity entity = convertToLevelEntity(dto);
            AcademicLevelsEntity levelSaved = repo.save(entity);
            return  convertToLevelsDTO(levelSaved);
        }catch (Exception e){
            log.error("Error al registrar un nuevo nivel academico " + e.getMessage());
            throw new ExceptionLevelNotValid("Error interno al guardar un nivel academico");
        }

    }

    public AcademicLevelsDTO updateLevel(String id, AcademicLevelsDTO json){
        AcademicLevelsEntity existsLevel = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("El ID no pudo ser encontrado."));
        existsLevel.setUniversityID(json.getUniversityID());
        existsLevel.setAcademicLevelName(json.getAcademicLevelName());

        AcademicLevelsEntity levelUpdated = repo.save(existsLevel);

        return convertToLevelsDTO(levelUpdated);
    }

    public boolean deleteLevel(String id){
        try{
            AcademicLevelsEntity existsLevel = repo.findById(id).orElse(null);

            if(existsLevel != null){
                repo.deleteById(id);
                return true;
            }else{
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se pudo encontrar el nivel academico con el ID: " + id + " para eliminar",1);
        }
    }
}
