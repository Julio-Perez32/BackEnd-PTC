package ptc2025.backend.Services.cyclicStudentPerformances;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.cyclicStudentPerformances.CyclicStudentPerformanceEntity;
import ptc2025.backend.Models.DTO.cyclicStudentPerformances.CyclicStudentPerformanceDTO;
import ptc2025.backend.Respositories.cyclicStudentPerformaces.CyclicStudentPerformanceRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CyclicStudentPerformanceService {

    @Autowired
    private CyclicStudentPerformanceRepository repo;

    public List<CyclicStudentPerformanceDTO> getPerformances(){
        return repo.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CyclicStudentPerformanceDTO insertPerformance(CyclicStudentPerformanceDTO dto){
        if(dto == null) throw new IllegalArgumentException("Rendimiento no puede ser nulo");
        if(repo.existsById(dto.getId())) throw new IllegalArgumentException("Ya existe un rendimiento con ese ID");
        try{
            CyclicStudentPerformanceEntity entity = convertToEntity(dto);
            return convertToDTO(repo.save(entity));
        } catch(Exception e){
            throw new RuntimeException("No se pudo crear rendimiento: " + e.getMessage());
        }
    }

    public CyclicStudentPerformanceDTO updatePerformance(String id, CyclicStudentPerformanceDTO dto){
        try{
            if(repo.existsById(id)){
                CyclicStudentPerformanceEntity entity = repo.getById(id);
                entity.setStudentId(dto.getStudentId());
                entity.setCycleCode(dto.getCycleCode());
                entity.setAcademicYearId(dto.getAcademicYearId());
                entity.setAverageGrade(dto.getAverageGrade());
                entity.setPassed(dto.getPassed());
                entity.setIsActive(dto.getIsActive());
                return convertToDTO(repo.save(entity));
            }
            throw new IllegalArgumentException("No se encontró rendimiento con ID: " + id);
        } catch(Exception e){
            throw new RuntimeException("No se pudo actualizar rendimiento: " + e.getMessage());
        }
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
        dto.setId(entity.getId());
        dto.setStudentId(entity.getStudentId());
        dto.setCycleCode(entity.getCycleCode());
        dto.setAcademicYearId(entity.getAcademicYearId());
        dto.setAverageGrade(entity.getAverageGrade());
        dto.setPassed(entity.getPassed());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }

    private CyclicStudentPerformanceEntity convertToEntity(CyclicStudentPerformanceDTO dto){
        CyclicStudentPerformanceEntity entity = new CyclicStudentPerformanceEntity();
        entity.setId(dto.getId());
        entity.setStudentId(dto.getStudentId());
        entity.setCycleCode(dto.getCycleCode());
        entity.setAcademicYearId(dto.getAcademicYearId());
        entity.setAverageGrade(dto.getAverageGrade());
        entity.setPassed(dto.getPassed());
        entity.setIsActive(dto.getIsActive());
        return entity;
    }
}
