package ptc2025.backend.Services.CourseOfferingsTeachers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.CourseOfferings.CourseOfferingsEntity;
import ptc2025.backend.Entities.CourseOfferingsTeachers.CourseOfferingsTeachersEntity;
import ptc2025.backend.Entities.employees.EmployeeEntity;
import ptc2025.backend.Exceptions.ExceptionBadRequest;
import ptc2025.backend.Exceptions.ExceptionNotFound;
import ptc2025.backend.Exceptions.ExceptionServerError;
import ptc2025.backend.Models.DTO.CourseOfferingsTeachers.CourseOfferingsTeachersDTO;
import ptc2025.backend.Respositories.CourseOfferings.CourseOfferingsRepository;
import ptc2025.backend.Respositories.CourseOfferingsTeachers.CourseOfferingsTeachersRepository;
import ptc2025.backend.Respositories.employees.EmployeeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CourseOfferingsTeachersService {

    @Autowired
    CourseOfferingsTeachersRepository repo;

    @Autowired
    EmployeeRepository repoEmployee;

    @Autowired
    CourseOfferingsRepository repoCourseOfferings;

    public List<CourseOfferingsTeachersDTO> getCourseTeachers(){
        List<CourseOfferingsTeachersEntity> teachers = repo.findAll();
        return teachers.stream()
                .map(this::convertToOfferingsTeachersDTO)
                .collect(Collectors.toList());
    }

    public Page<CourseOfferingsTeachersDTO> getCourseOfferingTeachersPagination(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<CourseOfferingsTeachersEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertToOfferingsTeachersDTO);
    }

    private CourseOfferingsTeachersDTO convertToOfferingsTeachersDTO(CourseOfferingsTeachersEntity entity) {
        CourseOfferingsTeachersDTO dto = new CourseOfferingsTeachersDTO();

        dto.setCourseOfferingTeacherID(entity.getCourseOfferingTeacherID());

        if (entity.getCourseOfferings() != null) {
            dto.setCourseOfferingID(entity.getCourseOfferings().getCourseOfferingID());

            if (entity.getCourseOfferings().getSubjectDefinitions() != null) {
                dto.setCoureOffering(entity.getCourseOfferings().getSubjectDefinitions().getSubjectName());
            } else {
                dto.setCoureOffering("Sin curso asignado");
            }
        } else {
            dto.setCourseOfferingID(null);
            dto.setCoureOffering("Sin curso asignado");
        }

        if (entity.getEmployee() != null) {
            dto.setEmployeeID(entity.getEmployee().getId());

            if (entity.getEmployee().getPeople() != null) {
                dto.setEmployee(
                        entity.getEmployee().getPeople().getFirstName() + " " +
                                entity.getEmployee().getPeople().getLastName()
                );
            } else {
                dto.setEmployee("Sin nombre asignado");
            }
        } else {
            dto.setEmployeeID(null);
            dto.setEmployee("Sin empleado asignado");
        }

        return dto;
    }


    private CourseOfferingsTeachersEntity convertToOfferingsTeachersEntity(CourseOfferingsTeachersDTO dto){
        CourseOfferingsTeachersEntity entity = new CourseOfferingsTeachersEntity();

        // Oferta
        CourseOfferingsEntity offering = repoCourseOfferings.findById(dto.getCourseOfferingID())
                .orElseThrow(() -> new ExceptionNotFound("Curso no encontrado con ID " + dto.getCourseOfferingID()));
        entity.setCourseOfferings(offering);

        // Empleado
        EmployeeEntity employee = repoEmployee.findById(dto.getEmployeeID())
                .orElseThrow(() -> new ExceptionNotFound("Empleado no encontrado con ID " + dto.getEmployeeID()));
        entity.setEmployee(employee);

        return entity;
    }


    public CourseOfferingsTeachersDTO insertOfferingTeacher(CourseOfferingsTeachersDTO dto){
        if (dto == null || dto.getCourseOfferingID() == null || dto.getCourseOfferingID().isBlank()
                || dto.getEmployeeID() == null || dto.getEmployeeID().isBlank()){
            throw new ExceptionBadRequest("Los campos deben de estar completos");
        }


        // si permites varios docentes por curso pero único por (curso, empleado):
        // if (repo.existsByCourseOfferings_CourseOfferingIDAndEmployee_Id(dto.getCourseOfferingID(), dto.getEmployeeID())) {
        //     throw new ExceptionBadRequest("El docente ya está asignado a este curso.");
        // }

        try {
            CourseOfferingsTeachersEntity entity = convertToOfferingsTeachersEntity(dto);
            CourseOfferingsTeachersEntity saved  = repo.save(entity);
            return convertToOfferingsTeachersDTO(saved);
        } catch (DataIntegrityViolationException ex) {
            // mapea violaciones de unique/fk a 409/400 en vez de 500
            throw new ExceptionBadRequest("Violación de integridad: " + ex.getMostSpecificCause().getMessage());
        } catch (Exception e){
            log.error("Error al registrar el docente", e);
            throw new ExceptionServerError("Error al registrar el docente");
        }
    }



    public CourseOfferingsTeachersDTO updateCourseTeacher(String ID, CourseOfferingsTeachersDTO json){
        CourseOfferingsTeachersEntity existsCourseTeacher = repo.findById(ID)
                .orElseThrow(() -> new ExceptionNotFound("Docente no encontrado"));

        if(json.getCourseOfferingID() != null){
            CourseOfferingsEntity courseOfferings = repoCourseOfferings.findById(json.getCourseOfferingID())
                    .orElseThrow(() -> new ExceptionNotFound("Curso no encontrado con ID " + json.getCourseOfferingID()));
            existsCourseTeacher.setCourseOfferings(courseOfferings);
        }

        if(json.getEmployeeID() != null){
            EmployeeEntity employee = repoEmployee.findById(json.getEmployeeID())
                    .orElseThrow(() -> new ExceptionNotFound("Empleado no encontrado con ID " + json.getEmployeeID()));
            existsCourseTeacher.setEmployee(employee);
        } else {
            existsCourseTeacher.setEmployee(null);
        }

        CourseOfferingsTeachersEntity savedCourseTeacher = repo.save(existsCourseTeacher);
        return convertToOfferingsTeachersDTO(savedCourseTeacher);
    }

    public boolean deleteCourseTeacher(String ID){
        try{
            CourseOfferingsTeachersEntity existsCourseTeacher = repo.findById(ID).orElse(null);
            if(existsCourseTeacher != null){
                repo.deleteById(ID);
                return true;
            } else {
                throw new ExceptionNotFound("No se encontró docente con ID: " + ID);
            }
        } catch (EmptyResultDataAccessException e){
            throw new ExceptionNotFound("No se encontró docente con ID: " + ID + " para eliminar");
        }
    }

}
