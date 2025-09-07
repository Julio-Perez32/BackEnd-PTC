package ptc2025.backend.Services.AcademicLevel;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Exceptions.ExceptionLevelNotValid;
import ptc2025.backend.Exceptions.ExceptionNotFound;
import ptc2025.backend.Exceptions.ExceptionServerError;
import ptc2025.backend.Exceptions.ExceptionValidationError;
import ptc2025.backend.Exceptions.ExcepcionDatosDuplicados;
import ptc2025.backend.Models.DTO.AcademicLevel.AcademicLevelsDTO;
import ptc2025.backend.Respositories.AcademicLevel.AcademicLevelsRepository;
import ptc2025.backend.Respositories.Universities.UniversityRespository;

import java.util.stream.Collectors;

@Slf4j
@Service
public class AcademicLevelsService {

    @Autowired
    AcademicLevelsRepository repo;

    @Autowired
    UniversityRespository repoUniversity;

    public Page<AcademicLevelsDTO> getAllAcademicLevels(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AcademicLevelsEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertToLevelsDTO);
    }

    public AcademicLevelsDTO convertToLevelsDTO(AcademicLevelsEntity level) {
        AcademicLevelsDTO dto = new AcademicLevelsDTO();
        dto.setAcademicLevelID(level.getAcademicLevelID());
        dto.setAcademicLevelName(level.getAcademicLevelName());
        if (level.getUniversity() != null) {
            dto.setUniversityName(level.getUniversity().getUniversityName());
            dto.setUniversityID(level.getUniversity().getUniversityID());
        } else {
            dto.setUniversityName("Sin Universidad Asignada");
            dto.setUniversityID(null);
        }
        return dto;
    }

    public AcademicLevelsEntity convertToLevelEntity(AcademicLevelsDTO dto) {
        AcademicLevelsEntity level = new AcademicLevelsEntity();
        level.setAcademicLevelID(dto.getAcademicLevelID());
        level.setAcademicLevelName(dto.getAcademicLevelName());
        if (dto.getUniversityID() != null) {
            UniversityEntity university = repoUniversity.findById(dto.getUniversityID())
                    .orElseThrow(() -> new IllegalArgumentException("Universidad no encontrada con ID: " + dto.getUniversityID()));
            level.setUniversity(university);
        }
        return level;
    }

    public AcademicLevelsDTO insertLevel(@Valid AcademicLevelsDTO dto) {

        //campos vacíos
        if (dto.getAcademicLevelName() == null || dto.getAcademicLevelName().isBlank()) {
            throw new ExceptionValidationError("El nombre del nivel académico es obligatorio.");
        }

        //campos duplicados
        boolean exists = repo.findAll().stream()
                .anyMatch(level -> level.getAcademicLevelName().equalsIgnoreCase(dto.getAcademicLevelName()) &&
                        level.getUniversity() != null &&
                        level.getUniversity().getUniversityID().equals(dto.getUniversityID()));
        if (exists) {
            throw new ExcepcionDatosDuplicados("El nivel académico ya existe para esta universidad.", "academicLevelName");
        }

        try {
            AcademicLevelsEntity entity = convertToLevelEntity(dto);
            AcademicLevelsEntity levelSaved = repo.save(entity);
            return convertToLevelsDTO(levelSaved);
        } catch (Exception e) {
            log.error("Error al registrar un nuevo nivel academico " + e.getMessage());
            throw new ExceptionLevelNotValid("Error interno al guardar un nivel academico");//error interno
        }
    }

    public AcademicLevelsDTO updateLevel(String id, AcademicLevelsDTO json) {
        AcademicLevelsEntity existsLevel = repo.findById(id)
                .orElseThrow(() -> new ExceptionNotFound("Nivel académico no encontrado con ID: " + id));

        //campos vacíos
        if (json.getAcademicLevelName() == null || json.getAcademicLevelName().isBlank()) {
            throw new ExceptionValidationError("El nombre del nivel académico es obligatorio.");
        }

        existsLevel.setAcademicLevelName(json.getAcademicLevelName());

        if (json.getUniversityID() != null) {
            UniversityEntity university = repoUniversity.findById(json.getUniversityID())
                    .orElseThrow(() -> new ExceptionNotFound("Universidad no encontrada con ID: " + json.getUniversityID()));
            existsLevel.setUniversity(university);
        } else {
            existsLevel.setUniversity(null);
        }

        AcademicLevelsEntity levelUpdated = repo.save(existsLevel);
        return convertToLevelsDTO(levelUpdated);
    }

    public boolean deleteLevel(String id) {
        AcademicLevelsEntity existsLevel = repo.findById(id)
                .orElseThrow(() -> new ExceptionNotFound("Nivel académico no encontrado con ID: " + id));

        try {
            repo.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionNotFound("No se pudo encontrar el nivel académico con el ID: " + id + " para eliminar");
        } catch (Exception e) {
            throw new ExceptionServerError("Error interno al eliminar el nivel académico.");
        }
    }
}
