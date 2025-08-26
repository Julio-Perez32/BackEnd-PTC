package ptc2025.backend.Services.CourseOfferingSchedules;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.CourseOfferingSchedules.CourseOfferingSchedulesEntity;
import ptc2025.backend.Entities.CourseOfferings.CourseOfferingsEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
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

    private CourseOfferingSchedulesDTO convertirADTO(CourseOfferingSchedulesEntity courseOfferingSchedules){
        CourseOfferingSchedulesDTO dto = new CourseOfferingSchedulesDTO();
        dto.setId(courseOfferingSchedules.getId());
        dto.setWeekday(courseOfferingSchedules.getWeekday());
        dto.setStartTime(courseOfferingSchedules.getStartTime());
        dto.setEndTime(courseOfferingSchedules.getEndTime());
        dto.setClassroom(courseOfferingSchedules.getClassroom());
        if(courseOfferingSchedules.getCourseOfferings() != null){
            dto.setCourseOfferingID(courseOfferingSchedules.getCourseOfferings().getCourseOfferingID());
        }else {
            dto.setCourseoffering("Sin courseOffering Asignada");
            dto.setCourseoffering(null);
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
        }catch (Exception e){
            log.error("Error al registrar el nuevo dato" + e.getMessage());
            throw new RuntimeException("Error al registrar el nuevo dato");
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
        CourseOfferingSchedulesEntity existente = repo.findById(id).orElseThrow(() -> new RuntimeException("Registro no encontrado"));
        existente.setWeekday(json.getWeekday());
        existente.setStartTime(json.getStartTime());
        existente.setEndTime(json.getEndTime());
        existente.setClassroom(json.getClassroom());

        if(json.getCourseOfferingID() != null){
            CourseOfferingsEntity courseOfferings = courseOfferingsRepo.findById(json.getCourseOfferingID())
                    .orElseThrow(() -> new IllegalArgumentException("courseOffering no encontrada con ID: " + json.getCourseOfferingID()));
            existente.setCourseOfferings(courseOfferings);
        }else {
            existente.setCourseOfferings(null);
        }

        CourseOfferingSchedulesEntity courseOfferingSchedulesActualizado = repo.save(existente);
        return convertirADTO(courseOfferingSchedulesActualizado);
    }

    public boolean eliminarcourseOfferingSchedule(String id) {
        try{
            CourseOfferingSchedulesEntity existente = repo.findById(id).orElse(null);
            if (existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro el registro", 1);
        }
    }
}
