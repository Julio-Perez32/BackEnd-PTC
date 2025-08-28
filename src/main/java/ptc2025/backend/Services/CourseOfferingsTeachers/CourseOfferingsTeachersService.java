package ptc2025.backend.Services.CourseOfferingsTeachers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.CourseOfferings.CourseOfferingsEntity;
import ptc2025.backend.Entities.CourseOfferingsTeachers.CourseOfferingsTeachersEntity;
import ptc2025.backend.Entities.employees.EmployeeEntity;
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

        // ID del CourseOfferingTeacher
        dto.setCourseOfferingTeacherID(entity.getCourseOfferingTeacherID());

        // CourseOffering
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

        // Employee
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

        if(dto.getEmployeeID() != null){
            EmployeeEntity employee = repoEmployee.findById(dto.getEmployeeID()).orElseThrow(
                    () -> new IllegalArgumentException("Empleado no encontrado con ID " + dto.getEmployeeID()));
            entity.setEmployee(employee);
        }
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

        if(json.getCourseOfferingID() != null){
            CourseOfferingsEntity courseOfferings = repoCourseOfferings.findById(json.getCourseOfferingID()).orElseThrow(
                    () -> new IllegalArgumentException("Curso no encontrado con ID " + json.getCourseOfferingID()));
            existsCourseTeacher.setCourseOfferings(courseOfferings);
        }

        if(json.getEmployeeID() != null){
            EmployeeEntity employee = repoEmployee.findById(json.getEmployeeID()).orElseThrow(
                    () -> new IllegalArgumentException("Empleado no encontrado con ID " + json.getEmployeeID()));
            existsCourseTeacher.setEmployee(employee);
        }else {
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
            }else{
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontró docente con ID: " + ID + " para eliminar",1);
        }
    }

}
