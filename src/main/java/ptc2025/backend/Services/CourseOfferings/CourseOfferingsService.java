package ptc2025.backend.Services.CourseOfferings;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.CourseOfferings.CourseOfferingsEntity;
import ptc2025.backend.Models.DTO.CourseOfferings.CourseOfferingsDTO;
import ptc2025.backend.Respositories.CourseOfferings.CourseOfferingsRepository;
import ptc2025.backend.Respositories.courseEnrollments.CourseEnrollmentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CourseOfferingsService {

    @Autowired
    CourseOfferingsRepository repo;

    public List<CourseOfferingsDTO> getAllCourses(){
        List<CourseOfferingsEntity> courses = repo.findAll();
        return courses.stream()
                .map(this::convertToCoursesDTO)
                .collect(Collectors.toList());
    }

    private CourseOfferingsDTO convertToCoursesDTO(CourseOfferingsEntity entity) {
        CourseOfferingsDTO dto = new CourseOfferingsDTO();
        dto.setCourseOfferingID(entity.getCourseOfferingID());
        dto.setSubjectID(entity.getSubjectID());
        dto.setYearCycleID(entity.getYearCycleID());
        return dto;
    }

    public CourseOfferingsEntity convertToCoursesEntity(CourseOfferingsDTO dto){
        CourseOfferingsEntity entity = new CourseOfferingsEntity();
        entity.setCourseOfferingID(dto.getCourseOfferingID());
        entity.setSubjectID(dto.getSubjectID());
        entity.setYearCycleID(dto.getYearCycleID());
        return entity;
    }

    public CourseOfferingsDTO insertCourse(CourseOfferingsDTO dto){
        if(dto == null || dto.getSubjectID() == null || dto.getSubjectID().isBlank() ||
        dto.getYearCycleID() == null || dto.getYearCycleID().isBlank()){
            throw new IllegalArgumentException("Todos los campos deben de estar llenos");
        }
        try{
            CourseOfferingsEntity entity = convertToCoursesEntity(dto);
            CourseOfferingsEntity saved = repo.save(entity);
            return convertToCoursesDTO(saved);
        }catch (Exception e){
            log.error("Error al registrar el curso: " + e.getMessage());
            throw new IllegalArgumentException("Error al registrar el curso" + e.getMessage());
        }
    }

    public CourseOfferingsDTO updateCourse(String id, CourseOfferingsDTO json){
        CourseOfferingsEntity exists = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Curso no encontrado"));
        exists.setSubjectID(json.getSubjectID());
        exists.setYearCycleID(json.getYearCycleID());

        CourseOfferingsEntity updatedCourse = repo.save(exists);
        return convertToCoursesDTO(updatedCourse);
    }

    public boolean deleteCourse(String id){
        try{
            CourseOfferingsEntity exists = repo.findById(id).orElse(null);
            if(exists != null){
                repo.deleteById(id);
                return true;
            }else{
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se econtró curso con ID: " + id + " para eliminar", 1);
        }
    }
}
