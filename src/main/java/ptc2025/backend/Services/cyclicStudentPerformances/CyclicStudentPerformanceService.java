package ptc2025.backend.Services.cyclicStudentPerformances;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
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

    public Page<CyclicStudentPerformanceDTO> getPerformancesPagination(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<CyclicStudentPerformanceEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertToDTO);
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
        CyclicStudentPerformanceEntity existente = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("El ID no pudo ser encontrado."));
        existente.setTotalValueUnits(dto.getTotalValueUnits());
        existente.setTotalMeritUnit(dto.getTotalMeritUnit());
        existente.setMeritUnitCoefficient(dto.getMeritUnitCoefficient());
        existente.setComputedAt(dto.getComputedAt());
        if(dto.getStudentCycleEnrollmentID() != null){
            StudentCycleEnrollmentEntity studentCycleEnrollment = studentCycleRepo.findById(dto.getStudentCycleEnrollmentID())
                    .orElseThrow(() -> new IllegalArgumentException("StudentcycleEnrollment no encontrada con ID: " + dto.getStudentCycleEnrollmentID()));
            existente.setStudentCycleEnrollment(studentCycleEnrollment);
        }else {
            existente.setStudentCycleEnrollment(null);
        }

        CyclicStudentPerformanceEntity levelUpdated = repo.save(existente);

        return convertToDTO(levelUpdated);
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
        dto.setTotalValueUnits(entity.getTotalValueUnits());
        dto.setTotalMeritUnit(entity.getTotalMeritUnit());
        dto.setMeritUnitCoefficient(entity.getMeritUnitCoefficient());
        dto.setComputedAt(entity.getComputedAt());
        if(entity.getStudentCycleEnrollment() != null){
            dto.setStudentCycleEnrollmentID(entity.getStudentCycleEnrollment().getId());
        }else {
            dto.setStudentCycleEnrollmentID("Sin Universidad Asignada");
            dto.setStudentCycleEnrollment(null);
        }

        return dto;
    }

    private CyclicStudentPerformanceEntity convertToEntity(CyclicStudentPerformanceDTO dto, StudentCycleEnrollmentEntity enrollment){
        CyclicStudentPerformanceEntity entity = new CyclicStudentPerformanceEntity();
        entity.setTotalValueUnits(dto.getTotalValueUnits());
        entity.setTotalMeritUnit(dto.getTotalMeritUnit());
        entity.setMeritUnitCoefficient(dto.getMeritUnitCoefficient());
        entity.setComputedAt(dto.getComputedAt());
        if(dto.getStudentCycleEnrollmentID() != null){
            StudentCycleEnrollmentEntity studentCycleEnrollment = studentCycleRepo.findById(dto.getStudentCycleEnrollmentID())
                    .orElseThrow(() -> new IllegalArgumentException("Universidad no encontrada con ID: " + dto.getStudentCycleEnrollmentID()));
            entity.setStudentCycleEnrollment(studentCycleEnrollment);
        }
        return entity;
    }
}
