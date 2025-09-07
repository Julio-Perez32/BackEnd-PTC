package ptc2025.backend.Services.careers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.DegreeTypes.DegreeTypesEntity;
import ptc2025.backend.Entities.Departments.DepartmentsEntity;
import ptc2025.backend.Entities.Localities.LocalitiesEntity;
import ptc2025.backend.Entities.Modalities.ModalitiesEntity;
import ptc2025.backend.Entities.careers.CareerEntity;
import ptc2025.backend.Exceptions.ExceptionAlreadyExists;
import ptc2025.backend.Exceptions.ExceptionBadRequest;
import ptc2025.backend.Exceptions.ExceptionNotFound;
import ptc2025.backend.Models.DTO.careers.CareerDTO;
import ptc2025.backend.Respositories.AcademicLevel.AcademicLevelsRepository;
import ptc2025.backend.Respositories.DegreeTypes.DegreeTypesRepository;
import ptc2025.backend.Respositories.Departments.DepartmentsRepository;
import ptc2025.backend.Respositories.Modalities.ModalityRepository;
import ptc2025.backend.Respositories.YearCycles.YearCyclesRepository;
import ptc2025.backend.Respositories.careers.CareerRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CareerService {

    @Autowired
    private CareerRepository repo;

    @Autowired
    private AcademicLevelsRepository academicRepo;

    @Autowired
    private DegreeTypesRepository degreeRepo;

    @Autowired
    private ModalityRepository modalityRepo;

    @Autowired
    private DepartmentsRepository departmentRepo;

    @Autowired
    private YearCyclesRepository yearCycleRepo;

    // GET
    public List<CareerDTO> getCareers() {
        return repo.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // GET DE PAGINACION
    public Page<CareerDTO> getCareersPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repo.findAll(pageable).map(this::convertToDTO);
    }


    // POST
    public CareerDTO insertCareer(CareerDTO dto) {
        if (dto == null)
            throw new ExceptionBadRequest("La carrera no puede ser nula");

        if (repo.existsById(dto.getId()))
            throw new ExceptionAlreadyExists("La carrera con el ID " + dto.getId() + " ya existe");

        CareerEntity entity = convertToEntity(dto);
        CareerEntity saved = repo.save(entity);
        return convertToDTO(saved);
    }

    // PUT
    public CareerDTO updateCareer(String id, CareerDTO dto) {
        CareerEntity exist = repo.findById(id)
                .orElseThrow(() -> new ExceptionNotFound("La carrera con el ID " + id + " no existe"));

        exist.setNameCareer(dto.getName());
        exist.setCareerCode(dto.getCareerCode());
        exist.setDescription(dto.getDescription());
        exist.setMinPassingScore(dto.getMinPassingScore());
        exist.setMinMUC(dto.getMinMUC());
        exist.setCompulsorySubjects(dto.getCompulsorySubjects());
        exist.setTotalValueUnits(dto.getTotalValueUnits());

        if (dto.getAcademicLevelId() != null) {
            AcademicLevelsEntity academicLevels = academicRepo.findById(dto.getAcademicLevelId())
                    .orElseThrow(() -> new ExceptionNotFound("Nivel académico no encontrado"));
            exist.setAcademicLevels(academicLevels);
        } else {
            exist.setAcademicLevels(null);
        }

        if (dto.getDegreeTypeId() != null) {
            DegreeTypesEntity degreeTypesEntity = degreeRepo.findById(dto.getDegreeTypeId())
                    .orElseThrow(() -> new ExceptionNotFound("Tipo de grado no encontrado"));
            exist.setDegreeTypes(degreeTypesEntity);
        } else {
            exist.setDegreeTypes(null);
        }

        if (dto.getModalityId() != null) {
            ModalitiesEntity modalities = modalityRepo.findById(dto.getModalityId())
                    .orElseThrow(() -> new ExceptionNotFound("Modalidad no encontrada"));
            exist.setModalities(modalities);
        } else {
            exist.setModalities(null);
        }

        if (dto.getDepartmentId() != null) {
            DepartmentsEntity department = departmentRepo.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new ExceptionNotFound("Departamento no encontrado"));
            exist.setDepartments(department);
        } else {
            exist.setDepartments(null);
        }

        CareerEntity actualizado = repo.save(exist);
        return convertToDTO(actualizado);
    }

    // DELETE
    public boolean deleteCareer(String id) {
        try {
            if (repo.existsById(id)) {
                repo.deleteById(id);
                return true;
            } else {
                throw new ExceptionNotFound("La carrera con ID " + id + " no existe");
            }
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionNotFound("La carrera con ID " + id + " no existe");
        }
    }


    // CONVERSIONES
    private CareerDTO convertToDTO(CareerEntity entity) {
        CareerDTO dto = new CareerDTO();
        dto.setId(entity.getId());
        dto.setAcademicLevelId(entity.getAcademicLevels().getAcademicLevelID());
        dto.setDegreeTypeId(entity.getDegreeTypes().getId());
        dto.setModalityId(entity.getModalities().getId());
        dto.setDepartmentId(entity.getDepartments().getDepartmentID());
        dto.setName(entity.getNameCareer());

        if(entity.getCareerCode() != null){
            dto.setAcademicLevelName(entity.getAcademicLevels().getAcademicLevelName());
            dto.setAcademicLevelId(entity.getAcademicLevels().getAcademicLevelID());
        }else{
            dto.setAcademicLevelName("Sin Nivel Academico Asignado");
            dto.setAcademicLevelId(null);
        }

        if(entity.getDegreeTypes() != null){
            dto.setDegreeTypeName(entity.getDegreeTypes().getDegreeTypeName());
            dto.setDegreeTypeName(entity.getDegreeTypes().getId());
        }else{
            dto.setDegreeTypeName("Sin Tipo de Nivel Academico Asignado");
            dto.setDegreeTypeId(null);
        }

        if(entity.getModalities() != null){
            dto.setModalityName(entity.getModalities().getModalityName());
            dto.setModalityId(entity.getModalities().getId());
        }else{
            dto.setModalityName("Sin Nivel Academico Asignado");
            dto.setModalityId(null);
        }

        if(entity.getDepartments() != null){
            dto.setDepartmentName(entity.getDepartments().getDepartmentName());
            dto.setDepartmentName(entity.getDepartments().getDepartmentID());
        }else{
            dto.setDepartmentName("Sin Departamente Asignado");
            dto.setDepartmentId(null);
        }

        dto.setCareerCode(entity.getCareerCode());
        dto.setDescription(entity.getDescription());
        dto.setMinPassingScore(entity.getMinPassingScore());
        dto.setMinMUC(entity.getMinMUC());
        dto.setCompulsorySubjects(entity.getCompulsorySubjects());
        dto.setTotalValueUnits(entity.getTotalValueUnits());
        return dto;
    }

    private CareerEntity convertToEntity(CareerDTO dto) {
        CareerEntity entity = new CareerEntity();
        entity.setNameCareer(dto.getName());
        entity.setCareerCode(dto.getCareerCode());
        entity.setDescription(dto.getDescription());
        entity.setMinPassingScore(dto.getMinPassingScore());
        entity.setMinMUC(dto.getMinMUC());
        entity.setCompulsorySubjects(dto.getCompulsorySubjects());
        entity.setTotalValueUnits(dto.getTotalValueUnits());
        if (dto.getAcademicLevelId() != null){
            AcademicLevelsEntity academicLevels = academicRepo.findById(dto.getAcademicLevelId())
                    .orElseThrow(() -> new IllegalArgumentException("Nivel académico no encontrado"));
            entity.setAcademicLevels(academicLevels);
        }
        if (dto.getDegreeTypeId() != null){
            DegreeTypesEntity degreeTypesEntity = degreeRepo.findById(dto.getDegreeTypeId())
                    .orElseThrow(() -> new IllegalArgumentException("Tipo de grado no encontrado"));
            entity.setDegreeTypes(degreeTypesEntity);
        }
        if (dto.getModalityId() != null){
            ModalitiesEntity modalities = modalityRepo.findById(dto.getModalityId())
                    .orElseThrow(() -> new IllegalArgumentException("Modalidad no encontrada"));
            entity.setModalities(modalities);
        }
        if (dto.getDepartmentId() != null){
            DepartmentsEntity modalities = departmentRepo.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new IllegalArgumentException("Departamento no encontrado"));
            entity.setDepartments(modalities);
        }


        return entity;
    }
}
