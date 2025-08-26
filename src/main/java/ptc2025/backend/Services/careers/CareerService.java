package ptc2025.backend.Services.careers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.careers.CareerEntity;
import ptc2025.backend.Models.DTO.careers.CareerDTO;
import ptc2025.backend.Respositories.AcademicLevel.AcademicLevelsRepository;
import ptc2025.backend.Respositories.DegreeTypes.DegreeTypesRepository;
import ptc2025.backend.Respositories.Departments.DepartmentsRepository;
import ptc2025.backend.Respositories.Modalities.ModalityRepository;
import ptc2025.backend.Respositories.careers.CareerRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CareerService {

    @Autowired private CareerRepository repo;
    @Autowired private AcademicLevelsRepository academicRepo;
    @Autowired private DegreeTypesRepository degreeRepo;
    @Autowired private ModalityRepository modalityRepo;
    @Autowired private DepartmentsRepository departmentRepo;

    // GET
    public List<CareerDTO> getCareers() {
        return repo.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // POST
    public CareerDTO insertCareer(CareerDTO dto) {
        if (dto == null) throw new IllegalArgumentException("Carrera no puede ser nula");
        if (repo.existsById(dto.getId())) throw new IllegalArgumentException("La carrera ya existe");

        CareerEntity entity = convertToEntity(dto);
        CareerEntity saved = repo.save(entity);
        return convertToDTO(saved);
    }

    // PUT
    public CareerDTO updateCareer(String id, CareerDTO dto) {
        if (!repo.existsById(id))
            throw new IllegalArgumentException("La carrera con ID " + id + " no existe");

        CareerEntity entity = convertToEntity(dto);
        entity.setId(id);
        CareerEntity saved = repo.save(entity);
        return convertToDTO(saved);
    }

    // DELETE
    public boolean deleteCareer(String id) {
        try {
            if (repo.existsById(id)) {
                repo.deleteById(id);
                return true;
            }
            return false;
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("La carrera con ID " + id + " no existe", 1);
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
        dto.setName(entity.getName());
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
        entity.setId(dto.getId());
        entity.setAcademicLevels(academicRepo.findById(dto.getAcademicLevelId())
                .orElseThrow(() -> new IllegalArgumentException("Nivel académico no encontrado")));
        entity.setDegreeTypes(degreeRepo.findById(dto.getDegreeTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de grado no encontrado")));
        entity.setModalities(modalityRepo.findById(dto.getModalityId())
                .orElseThrow(() -> new IllegalArgumentException("Modalidad no encontrada")));
        entity.setDepartments(departmentRepo.findById(dto.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("Departamento no encontrado")));
        entity.setName(dto.getName());
        entity.setCareerCode(dto.getCareerCode());
        entity.setDescription(dto.getDescription());
        entity.setMinPassingScore(dto.getMinPassingScore());
        entity.setMinMUC(dto.getMinMUC());
        entity.setCompulsorySubjects(dto.getCompulsorySubjects());
        entity.setTotalValueUnits(dto.getTotalValueUnits());
        return entity;
    }
}
