package ptc2025.backend.Services.CourseOfferingsTeachers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.CourseOfferingsTeachers.CourseOfferingsTeachersEntity;
import ptc2025.backend.Models.DTO.CourseOfferingsTeachers.CourseOfferingsTeachersDTO;
import ptc2025.backend.Respositories.CourseOfferings.CourseOfferingsRepository;
import ptc2025.backend.Respositories.CourseOfferingsTeachers.CourseOfferingsTeachersRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CourseOfferingsTeachersService {

    @Autowired
    CourseOfferingsTeachersRepository repo;

    public List<CourseOfferingsTeachersDTO> getCourseTeachers(){
        List<CourseOfferingsTeachersEntity> teachers = repo.findAll();
        return teachers.stream()
                .map(this::convertToOfferingsTeachersDTO)
                .collect(Collectors.toList());
    }

    private CourseOfferingsTeachersDTO convertToOfferingsTeachersDTO(CourseOfferingsTeachersEntity entity){
        CourseOfferingsTeachersDTO dto = new CourseOfferingsTeachersDTO();
        dto.setCourseOfferingID(entity.getCourseOfferingID());
        dto.setEmployeeID(entity.getEmployeeID());
        return dto;
    }

    private CourseOfferingsTeachersEntity convertToOfferingsTeachersEntity(CourseOfferingsTeachersDTO dto){
        CourseOfferingsTeachersEntity entity = new CourseOfferingsTeachersEntity();
        entity.setCourseOfferingID(dto.getCourseOfferingID());
        entity.setEmployeeID(dto.getEmployeeID());
        return entity;
    }

    public CourseOfferingsTeachersDTO insertOfferingTeacher(CourseOfferingsTeachersDTO dto){
        if (dto == null || dto.getCourseOfferingID() == null || dto.getCourseOfferingID().isEmpty() ||
                dto.getEmployeeID() == null || dto.getEmployeeID().isEmpty()){
            throw new IllegalArgumentException("Los campos deben de estar completos");
        }
        try{
            CourseOfferingsTeachersEntity entity = convertToOfferingsTeachersEntity(dto);
            CourseOfferingsTeachersEntity savedOfferingTeacher = repo.save(entity);
            return convertToOfferingsTeachersDTO(savedOfferingTeacher);
        }catch (Exception e){
            log.error("Error al registrar el docente: " + e.getMessage());
            throw new IllegalArgumentException("Error al registrar el docente");
        }
    }

    public CourseOfferingsTeachersDTO updateCourseTeacher(String ID, CourseOfferingsTeachersDTO json){
        CourseOfferingsTeachersEntity existsCourseTeacher = repo.findById(ID).orElseThrow(() -> new IllegalArgumentException("Docente no encontrado"));
        existsCourseTeacher.setCourseOfferingID(json.getCourseOfferingID());
        existsCourseTeacher.setEmployeeID(json.getEmployeeID());

        CourseOfferingsTeachersEntity savedCourseTeacher = repo.save(existsCourseTeacher);

        return convertToOfferingsTeachersDTO(savedCourseTeacher);
    }

    public boolean deleteCourseTeacher(String ID){
        try{
            CourseOfferingsTeachersEntity existsCourseTeacher = repo.findById(ID).orElse(null);
            if(existsCourseTeacher != null){
                repo.deleteById(ID);
                return true;
            }else{
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontró docente con ID: " + ID + " para eliminar",1);
        }
    }

}
