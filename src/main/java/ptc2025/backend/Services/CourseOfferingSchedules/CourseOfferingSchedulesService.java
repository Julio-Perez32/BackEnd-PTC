package ptc2025.backend.Services.CourseOfferingSchedules;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.CourseOfferingSchedules.CourseOfferingSchedulesEntity;
import ptc2025.backend.Entities.CourseOfferings.CourseOfferingsEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Exceptions.ExceptionNotFound;
import ptc2025.backend.Exceptions.ExceptionServerError;
import ptc2025.backend.Models.DTO.CourseOfferingSchedules.CourseOfferingSchedulesDTO;
import ptc2025.backend.Respositories.CourseOfferingSchedules.CourseOfferingSchedulesRepository;
import ptc2025.backend.Respositories.CourseOfferings.CourseOfferingsRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CourseOfferingSchedulesService {

    @Autowired
    private CourseOfferingSchedulesRepository repo;

    @Autowired
    CourseOfferingsRepository courseOfferingsRepo;

    public List<CourseOfferingSchedulesDTO> GetAllcourseOfferingSchedules() {
        List<CourseOfferingSchedulesEntity> Lista = repo.findAll();
        return Lista.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Page<CourseOfferingSchedulesDTO> getAllCourseOfferingSchedulesPagination(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<CourseOfferingSchedulesEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirADTO);
    }

    private CourseOfferingSchedulesDTO convertirADTO(CourseOfferingSchedulesEntity courseOfferingSchedules){
        CourseOfferingSchedulesDTO dto = new CourseOfferingSchedulesDTO();
        dto.setId(courseOfferingSchedules.getId());
        dto.setWeekday(courseOfferingSchedules.getWeekday());
        dto.setStartTime(courseOfferingSchedules.getStartTime());
        dto.setEndTime(courseOfferingSchedules.getEndTime());
        dto.setClassroom(courseOfferingSchedules.getClassroom());
        if (courseOfferingSchedules.getCourseOfferings() != null) {
            dto.setCourseoffering(
                    courseOfferingSchedules.getCourseOfferings().getSubjectDefinitions().getSubjectName()
            );
            dto.setCourseOfferingID(courseOfferingSchedules.getCourseOfferings().getCourseOfferingID());
        } else {
            dto.setCourseoffering("Sin courseOffering Asignada");
            dto.setCourseOfferingID(null);
        }

        return dto;
    }

    public CourseOfferingSchedulesDTO insertarDatos(CourseOfferingSchedulesDTO data) {
        if (data == null){
            throw new IllegalArgumentException("Campos no validos");
        }
        try {
            CourseOfferingSchedulesEntity entity = convertirAEntity(data);
            CourseOfferingSchedulesEntity courseOfferingSchedulesGuardado = repo.save(entity);
            return convertirADTO(courseOfferingSchedulesGuardado);
        } catch (Exception e) {
            log.error("Error al registrar el nuevo dato: " + e.getMessage());
            throw new ExceptionServerError("Error interno al registrar el nuevo dato", e);
        }
    }

    private CourseOfferingSchedulesEntity convertirAEntity(CourseOfferingSchedulesDTO data){
        CourseOfferingSchedulesEntity entity = new CourseOfferingSchedulesEntity();
        entity.setWeekday(data.getWeekday());
        entity.setStartTime(data.getStartTime());
        entity.setEndTime(data.getEndTime());
        entity.setClassroom(data.getClassroom());
        if(data.getCourseoffering() != null){
            CourseOfferingsEntity courseOfferings = courseOfferingsRepo.findById(data.getCourseOfferingID())
                    .orElseThrow(() -> new IllegalArgumentException("courseOffering no encontrada con ID: " + data.getCourseOfferingID()));
            entity.setCourseOfferings(courseOfferings);
        }
        return entity;
    }

    public CourseOfferingSchedulesDTO actualizarDatos(String id, CourseOfferingSchedulesDTO json) {
        try {
            CourseOfferingSchedulesEntity existente = repo.findById(id)
                    .orElseThrow(() -> new ExceptionNotFound("Registro no encontrado con ID: " + id));

            existente.setWeekday(json.getWeekday());
            existente.setStartTime(json.getStartTime());
            existente.setEndTime(json.getEndTime());
            existente.setClassroom(json.getClassroom());

            if(json.getCourseOfferingID() != null){
                CourseOfferingsEntity courseOfferings = courseOfferingsRepo.findById(json.getCourseOfferingID())
                        .orElseThrow(() -> new IllegalArgumentException("courseOffering no encontrada con ID: " + json.getCourseOfferingID()));
                existente.setCourseOfferings(courseOfferings);
            } else {
                existente.setCourseOfferings(null);
            }

            CourseOfferingSchedulesEntity courseOfferingSchedulesActualizado = repo.save(existente);
            return convertirADTO(courseOfferingSchedulesActualizado);
        } catch (ExceptionNotFound e) {
            throw e; // se propaga directamente
        } catch (Exception e) {
            log.error("Error al actualizar el registro: " + e.getMessage());
            throw new ExceptionServerError("Error interno al actualizar el registro", e);
        }
    }

    public boolean eliminarcourseOfferingSchedule(String id) {
        try {
            CourseOfferingSchedulesEntity existente = repo.findById(id)
                    .orElseThrow(() -> new ExceptionNotFound("No se encontró el registro con ID: " + id));

            repo.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionNotFound("No se encontró el registro con ID: " + id);
        } catch (Exception e) {
            log.error("Error al eliminar el registro: " + e.getMessage());
            throw new ExceptionServerError("Error interno al eliminar el registro", e);
        }
    }
}
