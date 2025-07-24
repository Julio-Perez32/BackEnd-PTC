package ptc2025.backend.Services.courseOfferingSchedules;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.courseOfferingSchedules.courseOfferingSchedulesEntity;
import ptc2025.backend.Models.DTO.courseOfferingSchedules.courseOfferingSchedulesDTO;
import ptc2025.backend.Respositories.courseOfferingSchedules.courseOfferingScheduleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class courseOfferingScheduleService {

    @Autowired
    private courseOfferingScheduleRepository repo;

    public List<courseOfferingSchedulesDTO> GetAllcourseOfferingSchedules() {
        List<courseOfferingSchedulesEntity> Lista = repo.findAll();
        return Lista.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private courseOfferingSchedulesDTO convertirADTO(courseOfferingSchedulesEntity courseOfferingSchedules){
        courseOfferingSchedulesDTO dto = new courseOfferingSchedulesDTO();
        dto.setId(courseOfferingSchedules.getId());
        dto.setCourseOfferingID(courseOfferingSchedules.getCourseOfferingID());
        dto.setWeekday(courseOfferingSchedules.getWeekday());
        dto.setStartTime(courseOfferingSchedules.getStartTime());
        dto.setEndTime(courseOfferingSchedules.getEndTime());
        dto.setClassroom(courseOfferingSchedules.getClassroom());
        return dto;
    }

    public courseOfferingSchedulesDTO insertarDatos(courseOfferingSchedulesDTO data) {
        if (data == null){
            throw new IllegalArgumentException("Campos no validos");
        }
        try {
            courseOfferingSchedulesEntity entity = convertirAEntity(data);
            courseOfferingSchedulesEntity courseOfferingSchedulesGuardado = repo.save(entity);
            return convertirADTO(courseOfferingSchedulesGuardado);
        }catch (Exception e){
            log.error("Error al registrar el nuevo dato" + e.getMessage());
            throw new RuntimeException("Error al registrar el nuevo dato");
        }
    }

    private courseOfferingSchedulesEntity convertirAEntity(courseOfferingSchedulesDTO data){
        courseOfferingSchedulesEntity entity = new courseOfferingSchedulesEntity();
        entity.setCourseOfferingID(data.getCourseOfferingID());
        entity.setWeekday(data.getWeekday());
        entity.setStartTime(data.getStartTime());
        entity.setEndTime(data.getEndTime());
        entity.setClassroom(data.getClassroom());
        return entity;
    }

    public courseOfferingSchedulesDTO actualizarDatos(String id, courseOfferingSchedulesDTO json) {
        courseOfferingSchedulesEntity existente = repo.findById(id).orElseThrow(() -> new RuntimeException("Registro no encontrado"));
        existente.setCourseOfferingID(json.getCourseOfferingID());
        existente.setWeekday(json.getWeekday());
        existente.setStartTime(json.getStartTime());
        existente.setEndTime(json.getEndTime());
        existente.setClassroom(json.getClassroom());
        courseOfferingSchedulesEntity courseOfferingSchedulesActualizado = repo.save(existente);
        return convertirADTO(courseOfferingSchedulesActualizado);
    }

    public boolean eliminarcourseOfferingSchedule(String id) {
        try{
            courseOfferingSchedulesEntity existente = repo.findById(id).orElse(null);
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
