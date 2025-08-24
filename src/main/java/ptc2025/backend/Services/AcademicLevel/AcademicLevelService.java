package ptc2025.backend.Services.AcademicLevel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Execeptions.ExceptionLevelNotValid;
import ptc2025.backend.Models.DTO.AcademicLevel.AcademicLevelsDTO;
import ptc2025.backend.Respositories.AcademicLevel.AcademicLevelsRepository;
import ptc2025.backend.Respositories.Universities.UniversityRespository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AcademicLevelService {

    @Autowired
    AcademicLevelsRepository repo;

    @Autowired //Se inyecta el repositorio de University
    UniversityRespository repoUniversity;

    public List<AcademicLevelsDTO> getAllLevels(){
        List<AcademicLevelsEntity> levels = repo.findAll();
        return levels.stream()
                .map(this::convertToLevelsDTO)
                .collect(Collectors.toList());
    }

    public AcademicLevelsDTO convertToLevelsDTO(AcademicLevelsEntity level){
        AcademicLevelsDTO dto = new AcademicLevelsDTO();
        dto.setAcademicLevelID(level.getAcademicLevelID());
        dto.setAcademicLevelName(level.getAcademicLevelName());
        if(level.getUniversity() != null){
            dto.setUniversityName(level.getUniversity().getUniversityName());
            dto.setUniversityID(level.getUniversity().getUniversityID());
        }else {
            dto.setUniversityName("Sin Universidad Asignada");
            dto.setUniversityID(null);
        }
        return dto;
    }

    public AcademicLevelsEntity convertToLevelEntity (AcademicLevelsDTO dto){
        AcademicLevelsEntity level = new AcademicLevelsEntity();
        level.setAcademicLevelID(dto.getAcademicLevelID());
        level.setAcademicLevelName(dto.getAcademicLevelName());
        if(dto.getUniversityID() != null){
            UniversityEntity university = repoUniversity.findById(dto.getUniversityID())
                    .orElseThrow(() -> new IllegalArgumentException("Universidad no encontrada con ID: " + dto.getUniversityID()));
            level.setUniversity(university);
        }
        return level;
    }

    public AcademicLevelsDTO insertLevel(AcademicLevelsDTO dto){

        if (dto.getAcademicLevelName() == null || dto.getAcademicLevelName().isBlank()){
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
        existsLevel.setAcademicLevelName(json.getAcademicLevelName());

        if(json.getUniversityID() != null){
            UniversityEntity university = repoUniversity.findById(json.getUniversityID())
                    .orElseThrow(() -> new IllegalArgumentException("Universidad no encontrada con ID: " + json.getUniversityID()));
            existsLevel.setUniversity(university);
        }else {
            existsLevel.setUniversity(null);
        }

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
