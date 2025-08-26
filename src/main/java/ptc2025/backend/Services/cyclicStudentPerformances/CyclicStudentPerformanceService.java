package ptc2025.backend.Services.cyclicStudentPerformances;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.cyclicStudentPerformances.CyclicStudentPerformanceEntity;
import ptc2025.backend.Entities.studentCycleEnrollments.StudentCycleEnrollmentEntity;
import ptc2025.backend.Models.DTO.cyclicStudentPerformances.CyclicStudentPerformanceDTO;
import ptc2025.backend.Respositories.cyclicStudentPerformaces.CyclicStudentPerformanceRepository;
import ptc2025.backend.Respositories.studentCycleEnrollments.StudentCycleEnrollmentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CyclicStudentPerformanceService {

    @Autowired
    private CyclicStudentPerformanceRepository repo;

    @Autowired
    private StudentCycleEnrollmentRepository studentCycleRepo;

    public List<CyclicStudentPerformanceDTO> getPerformances(){
        return repo.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CyclicStudentPerformanceDTO insertPerformance(CyclicStudentPerformanceDTO dto){
        if(dto == null) throw new IllegalArgumentException("Rendimiento no puede ser nulo");
        if(repo.existsById(dto.getPerformanceID())) throw new IllegalArgumentException("Ya existe un rendimiento con ese ID");
        StudentCycleEnrollmentEntity enrollment = studentCycleRepo.findById(dto.getStudentCycleEnrollmentID())
                .orElseThrow(() -> new IllegalArgumentException("No existe StudentCycleEnrollment con ID: " + dto.getStudentCycleEnrollmentID()));

        CyclicStudentPerformanceEntity entity = convertToEntity(dto, enrollment);
        return convertToDTO(repo.save(entity));
    }

    public CyclicStudentPerformanceDTO updatePerformance(String id, CyclicStudentPerformanceDTO dto){
        if(!repo.existsById(id)) throw new IllegalArgumentException("No se encontró rendimiento con ID: " + id);

        StudentCycleEnrollmentEntity enrollment = studentCycleRepo.findById(dto.getStudentCycleEnrollmentID())
                .orElseThrow(() -> new IllegalArgumentException("No existe StudentCycleEnrollment con ID: " + dto.getStudentCycleEnrollmentID()));

        CyclicStudentPerformanceEntity entity = convertToEntity(dto, enrollment);
        entity.setPerformanceID(id);
        return convertToDTO(repo.save(entity));
    }

    public boolean deletePerformance(String id){
        try{
            if(repo.existsById(id)){
                repo.deleteById(id);
                return true;
            }
            return false;
        } catch(EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("El rendimiento con id "+ id + " no existe", 1);
        }
    }

    private CyclicStudentPerformanceDTO convertToDTO(CyclicStudentPerformanceEntity entity){
        CyclicStudentPerformanceDTO dto = new CyclicStudentPerformanceDTO();
        dto.setPerformanceID(entity.getPerformanceID());
        dto.setStudentCycleEnrollmentID(entity.getStudentCycleEnrollment().getId());
        dto.setTotalValueUnits(entity.getTotalValueUnits());
        dto.setTotalMeritUnit(entity.getTotalMeritUnit());
        dto.setMeritUnitCoefficient(entity.getMeritUnitCoefficient());
        dto.setComputedAt(entity.getComputedAt());

        // ⚡ Derivados (supone getters en StudentCycleEnrollmentEntity)
        dto.setStudentID(entity.getStudentCycleEnrollment().getStudentCareerEnrollment().getStudentCareerEnrollmentID());
        dto.setStudentName(entity.getStudentCycleEnrollment().getStudentCareerEnrollment().getStudentCareerEnrollmentID());
        dto.setCareerID(entity.getStudentCycleEnrollment().getStudentCareerEnrollment().getStudentCareerEnrollmentID());
        dto.setCareerName(entity.getStudentCycleEnrollment().getStudentCareerEnrollment().getStudentCareerEnrollmentID());

        return dto;
    }

    private CyclicStudentPerformanceEntity convertToEntity(CyclicStudentPerformanceDTO dto, StudentCycleEnrollmentEntity enrollment){
        CyclicStudentPerformanceEntity entity = new CyclicStudentPerformanceEntity();
        entity.setPerformanceID(dto.getPerformanceID());
        entity.setStudentCycleEnrollment(enrollment);
        entity.setTotalValueUnits(dto.getTotalValueUnits());
        entity.setTotalMeritUnit(dto.getTotalMeritUnit());
        entity.setMeritUnitCoefficient(dto.getMeritUnitCoefficient());
        entity.setComputedAt(dto.getComputedAt());
        return entity;
    }
}
