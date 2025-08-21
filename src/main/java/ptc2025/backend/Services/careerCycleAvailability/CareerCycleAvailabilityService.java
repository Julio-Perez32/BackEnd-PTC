package ptc2025.backend.Services.careerCycleAvailability;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.careerCycleAvailability.CareerCycleAvailabilityEntity;
import ptc2025.backend.Models.DTO.careerCycleAvailability.CareerCycleAvailabilityDTO;
import ptc2025.backend.Respositories.careerCycleAvailability.CareerCycleAvailabilityRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CareerCycleAvailabilityService {

    @Autowired
    private CareerCycleAvailabilityRepository repo;

    public List<CareerCycleAvailabilityDTO> getAvailability(){
        return repo.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CareerCycleAvailabilityDTO insertAvailability(CareerCycleAvailabilityDTO dto){
        if(dto == null) throw new IllegalArgumentException("Disponibilidad no puede ser nula");
        if(repo.existsById(dto.getId())) throw new IllegalArgumentException("Ya existe disponibilidad con ese ID");
        try{
            CareerCycleAvailabilityEntity entity = convertToEntity(dto);
            return convertToDTO(repo.save(entity));
        } catch(Exception e){
            throw new RuntimeException("No se pudo crear disponibilidad: " + e.getMessage());
        }
    }

    public CareerCycleAvailabilityDTO updateAvailability(String id, CareerCycleAvailabilityDTO dto){
        try{
            if(repo.existsById(id)){
                CareerCycleAvailabilityEntity entity = repo.getById(id);
                entity.setCareerId(dto.getCareerId());
                entity.setAcademicYearId(dto.getAcademicYearId());
                entity.setCycleCode(dto.getCycleCode());
                entity.setMaxCapacity(dto.getMaxCapacity());
                entity.setIsActive(dto.getIsActive());
                return convertToDTO(repo.save(entity));
            }
            throw new IllegalArgumentException("No se encontró disponibilidad con ID: " + id);
        } catch(Exception e){
            throw new RuntimeException("No se pudo actualizar disponibilidad: " + e.getMessage());
        }
    }

    public boolean deleteAvailability(String id){
        try{
            if(repo.existsById(id)){
                repo.deleteById(id);
                return true;
            }
            return false;
        } catch(EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("La disponibilidad con id "+ id + " no existe", 1);
        }
    }

    private CareerCycleAvailabilityDTO convertToDTO(CareerCycleAvailabilityEntity entity){
        CareerCycleAvailabilityDTO dto = new CareerCycleAvailabilityDTO();
        dto.setId(entity.getId());
        dto.setCareerId(entity.getCareerId());
        dto.setAcademicYearId(entity.getAcademicYearId());
        dto.setCycleCode(entity.getCycleCode());
        dto.setMaxCapacity(entity.getMaxCapacity());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }

    private CareerCycleAvailabilityEntity convertToEntity(CareerCycleAvailabilityDTO dto){
        CareerCycleAvailabilityEntity entity = new CareerCycleAvailabilityEntity();
        entity.setId(dto.getId());
        entity.setCareerId(dto.getCareerId());
        entity.setAcademicYearId(dto.getAcademicYearId());
        entity.setCycleCode(dto.getCycleCode());
        entity.setMaxCapacity(dto.getMaxCapacity());
        entity.setIsActive(dto.getIsActive());
        return entity;
    }
}
