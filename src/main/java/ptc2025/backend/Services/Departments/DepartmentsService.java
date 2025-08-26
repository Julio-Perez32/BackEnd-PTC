package ptc2025.backend.Services.Departments;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.Departments.DepartmentsEntity;
import ptc2025.backend.Entities.Faculties.FacultiesEntity;
import ptc2025.backend.Models.DTO.Departments.DepartmentsDTO;
import ptc2025.backend.Respositories.Departments.DepartmentsRepository;
import ptc2025.backend.Respositories.Faculties.FacultiesRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DepartmentsService {

    @Autowired
    DepartmentsRepository repo;

    @Autowired
    FacultiesRepository repoFaculties;

    public List<DepartmentsDTO> getAllDepartments(){
        List<DepartmentsEntity> departments = repo.findAll();
        return departments.stream()
                .map(this::convertToDepartmentsDTO)
                .collect(Collectors.toList());
    }

    private DepartmentsDTO convertToDepartmentsDTO(DepartmentsEntity departments){
        DepartmentsDTO dto = new DepartmentsDTO();
        dto.setDepartmentID(departments.getDepartmentID());
        dto.setDepartmentName(departments.getDepartmentName());
        dto.setDepartmentType(departments.getDepartmentType());

        if(departments.getFaculty() != null){
            dto.setFacultyName(departments.getFaculty().getFacultyName());
            dto.setFacultyID(departments.getFaculty().getFacultyID());
        }else{
            dto.setFacultyName("Sin facultad asignada");
            dto.setFacultyID(null);
        }
        return dto;
    }

    private DepartmentsEntity convertToDepartmentEntity(DepartmentsDTO dto){
        DepartmentsEntity entity = new DepartmentsEntity();
        entity.setDepartmentID(dto.getDepartmentID());
        entity.setDepartmentName(dto.getDepartmentName());
        entity.setDepartmentType(dto.getDepartmentType());

        if(dto.getFacultyID() != null){
            FacultiesEntity faculties = repoFaculties.findById(dto.getFacultyID()).orElseThrow(
                    () -> new IllegalArgumentException("Facultad no encontrada con ID" + dto.getFacultyID()));
            entity.setFaculty(faculties);
        }
        return entity;
    }

    public DepartmentsDTO insertDepartment(DepartmentsDTO dto){
        if (dto == null || dto.getFacultyID() == null || dto.getFacultyID().isEmpty() || dto.getDepartmentName() == null
                || dto.getDepartmentName().isEmpty() || dto.getDepartmentType() == null || dto.getDepartmentType().isEmpty()){
            throw new IllegalArgumentException("Todos los campos deben de estar llenos.");
        }
        try{
            DepartmentsEntity entity = convertToDepartmentEntity(dto);
            DepartmentsEntity departmentSaved = repo.save(entity);
            return convertToDepartmentsDTO(departmentSaved);
        }catch (Exception e){
            log.error("Error al registrar: " + e.getMessage());
            throw new IllegalArgumentException("Error al registrar el departamento");
        }
    }

    public DepartmentsDTO updateDepartment(String id, DepartmentsDTO json){
        DepartmentsEntity existsDepartment = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Departamento no encontrado."));
        existsDepartment.setFacultyID(json.getFacultyID());
        existsDepartment.setDepartmentName(json.getDepartmentName());
        existsDepartment.setDepartmentType(json.getDepartmentType());

        DepartmentsEntity departmentUpdated = repo.save(existsDepartment);

        return convertToDepartmentsDTO(departmentUpdated);
    }

    public boolean deleteDepartment(String id){
        try{
            DepartmentsEntity existsDepartment = repo.findById(id).orElse(null);
            if (existsDepartment != null){
                repo.deleteById(id);
                return true;
            }else{
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se pudo encontrar un departemento con el ID: " + id + " para eliminar", 1);
        }
    }
}
