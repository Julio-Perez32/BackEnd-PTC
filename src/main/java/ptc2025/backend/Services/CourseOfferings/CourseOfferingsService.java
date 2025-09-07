package ptc2025.backend.Services.CourseOfferings;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.CourseOfferings.CourseOfferingsEntity;
import ptc2025.backend.Entities.SubjectDefinitions.SubjectDefinitionsEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Entities.YearCycles.YearCyclesEntity;
import ptc2025.backend.Exceptions.ExceptionBadRequest;
import ptc2025.backend.Exceptions.ExceptionNotFound;
import ptc2025.backend.Exceptions.ExceptionServerError;
import ptc2025.backend.Models.DTO.CourseOfferings.CourseOfferingsDTO;
import ptc2025.backend.Respositories.CourseOfferings.CourseOfferingsRepository;
import ptc2025.backend.Respositories.SubjectDefinitions.SubjectDefinitionsRepository;
import ptc2025.backend.Respositories.YearCycles.YearCyclesRepository;
import ptc2025.backend.Respositories.courseEnrollments.CourseEnrollmentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CourseOfferingsService {

    @Autowired
    CourseOfferingsRepository repo;

    @Autowired
    SubjectDefinitionsRepository subjectDefinitionsRepo;

    @Autowired
    YearCyclesRepository yearCyclesRepo;

    // Obtener todos los cursos
    public List<CourseOfferingsDTO> getAllCourses() {
        List<CourseOfferingsEntity> courses = repo.findAll();
        return courses.stream()
                .map(this::convertToCoursesDTO)
                .collect(Collectors.toList());
    }

    // Paginación
    public Page<CourseOfferingsDTO> getAllCoursesPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CourseOfferingsEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertToCoursesDTO);
    }

    // Conversión a DTO
    private CourseOfferingsDTO convertToCoursesDTO(CourseOfferingsEntity entity) {
        CourseOfferingsDTO dto = new CourseOfferingsDTO();
        dto.setCourseOfferingID(entity.getCourseOfferingID());

        if (entity.getSubjectDefinitions() != null) {
            dto.setSubject(entity.getSubjectDefinitions().getSubjectName());
            dto.setSubjectID(entity.getSubjectDefinitions().getSubjectID());
        } else {
            dto.setSubject("Sin Materia Asignada");
            dto.setSubjectID(null);
        }

        if (entity.getYearCycles() != null) {
            dto.setYearCycleID(entity.getYearCycles().getId());
            dto.setYearcycleName(
                    entity.getYearCycles().getStartDate() + " a " + entity.getYearCycles().getEndDate()
            );
        } else {
            dto.setYearcycleName("Sin Año Asignado");
            dto.setYearCycleID(null);
        }

        return dto;
    }

    // Conversión a Entity
    public CourseOfferingsEntity convertToCoursesEntity(CourseOfferingsDTO dto) {
        CourseOfferingsEntity entity = new CourseOfferingsEntity();
        entity.setCourseOfferingID(dto.getCourseOfferingID());

        if (dto.getSubjectID() != null) {
            SubjectDefinitionsEntity subjectDefinitions = subjectDefinitionsRepo.findById(dto.getSubjectID())
                    .orElseThrow(() -> new ExceptionNotFound("Materia no encontrada con ID: " + dto.getSubjectID()));
            entity.setSubjectDefinitions(subjectDefinitions);
        }

        if (dto.getYearCycleID() != null) {
            YearCyclesEntity yearCycles = yearCyclesRepo.findById(dto.getYearCycleID())
                    .orElseThrow(() -> new ExceptionNotFound("Año no encontrado con ID: " + dto.getYearCycleID()));
            entity.setYearCycles(yearCycles);
        }
        return entity;
    }

    // Insertar
    public CourseOfferingsDTO insertCourse(CourseOfferingsDTO dto) {
        if (dto == null || dto.getSubjectID() == null || dto.getSubjectID().isBlank() ||
                dto.getYearCycleID() == null || dto.getYearCycleID().isBlank()) {
            throw new ExceptionBadRequest("Todos los campos deben de estar llenos");
        }
        try {
            CourseOfferingsEntity entity = convertToCoursesEntity(dto);
            CourseOfferingsEntity saved = repo.save(entity);
            return convertToCoursesDTO(saved);
        } catch (Exception e) {
            log.error("Error al registrar el curso: " + e.getMessage());
            throw new ExceptionServerError("Error interno al registrar el curso: " + e.getMessage());
        }
    }

    // Actualizar
    public CourseOfferingsDTO updateCourse(String id, CourseOfferingsDTO json) {
        CourseOfferingsEntity exists = repo.findById(id)
                .orElseThrow(() -> new ExceptionNotFound("Curso no encontrado con ID: " + id));

        if (json.getSubjectID() != null) {
            SubjectDefinitionsEntity subjectDefinitions = subjectDefinitionsRepo.findById(json.getSubjectID())
                    .orElseThrow(() -> new ExceptionNotFound("Materia no encontrada con ID: " + json.getSubjectID()));
            exists.setSubjectDefinitions(subjectDefinitions);
        } else {
            exists.setSubjectDefinitions(null);
        }

        if (json.getYearCycleID() != null) {
            YearCyclesEntity yearCycles = yearCyclesRepo.findById(json.getYearCycleID())
                    .orElseThrow(() -> new ExceptionNotFound("Año no encontrado con ID: " + json.getYearCycleID()));
            exists.setYearCycles(yearCycles);
        } else {
            exists.setYearCycles(null);
        }

        try {
            CourseOfferingsEntity updatedCourse = repo.save(exists);
            return convertToCoursesDTO(updatedCourse);
        } catch (Exception e) {
            log.error("Error al actualizar el curso: " + e.getMessage());
            throw new ExceptionServerError("Error interno al actualizar el curso: " + e.getMessage());
        }
    }

    // Eliminar
    public boolean deleteCourse(String id) {
        try {
            CourseOfferingsEntity exists = repo.findById(id)
                    .orElseThrow(() -> new ExceptionNotFound("Curso no encontrado con ID: " + id));

            repo.deleteById(id);
            return true;

        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionNotFound("Curso no encontrado con ID: " + id + " para eliminar");
        } catch (Exception e) {
            log.error("Error al eliminar el curso: " + e.getMessage());
            throw new ExceptionServerError("Error interno al eliminar el curso: " + e.getMessage());
        }
    }
}
