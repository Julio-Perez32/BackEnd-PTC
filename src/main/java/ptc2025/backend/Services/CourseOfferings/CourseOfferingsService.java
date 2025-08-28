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


    public List<CourseOfferingsDTO> getAllCourses(){
        List<CourseOfferingsEntity> courses = repo.findAll();
        return courses.stream()
                .map(this::convertToCoursesDTO)
                .collect(Collectors.toList());
    }

    public Page<CourseOfferingsDTO> getAllCoursesPagination(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<CourseOfferingsEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertToCoursesDTO);
    }

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
            // Concatenamos startDate y endDate para mostrar el rango
            dto.setYearcycleName(
                    entity.getYearCycles().getStartDate() + " a " + entity.getYearCycles().getEndDate()
            );
        } else {
            dto.setYearcycleName("Sin Año Asignado");
            dto.setYearCycleID(null);
        }

        return dto;
    }


    public CourseOfferingsEntity convertToCoursesEntity(CourseOfferingsDTO dto){
        CourseOfferingsEntity entity = new CourseOfferingsEntity();
        entity.setCourseOfferingID(dto.getCourseOfferingID());

        if(dto.getSubjectID() != null){
            SubjectDefinitionsEntity subjectDefinitions = subjectDefinitionsRepo.findById(dto.getSubjectID())
                    .orElseThrow(() -> new IllegalArgumentException("Materia no encontrada con ID: " + dto.getSubjectID()));
            entity.setSubjectDefinitions(subjectDefinitions);
        }

        if(dto.getYearCycleID() != null){
            YearCyclesEntity yearCycles = yearCyclesRepo.findById(dto.getYearCycleID())
                    .orElseThrow(() -> new IllegalArgumentException("Año no encontrado con ID: " + dto.getYearCycleID()));
            entity.setYearCycles(yearCycles);
        }
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

        if(json.getSubjectID() != null){
            SubjectDefinitionsEntity subjectDefinitions = subjectDefinitionsRepo.findById(json.getSubjectID())
                    .orElseThrow(() -> new IllegalArgumentException("Materia no encontrada con ID: " + json.getSubjectID()));
            exists.setSubjectDefinitions(subjectDefinitions);
        }else {
            exists.setSubjectDefinitions(null);
        }

        if(json.getYearCycleID() != null){
            YearCyclesEntity yearCycles = yearCyclesRepo.findById(json.getYearCycleID())
                    .orElseThrow(() -> new IllegalArgumentException("año no encontrado con ID: " + json.getYearCycleID()));
            exists.setYearCycles(yearCycles);
        }else {
            exists.setYearCycles(null);
        }

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
