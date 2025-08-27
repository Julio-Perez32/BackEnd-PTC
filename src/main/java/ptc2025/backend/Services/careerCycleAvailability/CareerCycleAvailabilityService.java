package ptc2025.backend.Services.careerCycleAvailability;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.AcademicYear.AcademicYearEntity;
import ptc2025.backend.Entities.CycleTypes.CycleTypesEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Entities.YearCycles.YearCyclesEntity;
import ptc2025.backend.Entities.careerCycleAvailability.CareerCycleAvailabilityEntity;
import ptc2025.backend.Entities.careers.CareerEntity;
import ptc2025.backend.Models.DTO.careerCycleAvailability.CareerCycleAvailabilityDTO;
import ptc2025.backend.Respositories.YearCycles.YearCyclesRepository;
import ptc2025.backend.Respositories.careerCycleAvailability.CareerCycleAvailabilityRepository;
import ptc2025.backend.Respositories.careers.CareerRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j

public class CareerCycleAvailabilityService {

    @Autowired
    private CareerCycleAvailabilityRepository repo;

    @Autowired
    private CareerRepository careerRepository;

    @Autowired
    private YearCyclesRepository yearCyclesRepo;

    public List<CareerCycleAvailabilityDTO> getAvailability() {
        return repo.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Page<CareerCycleAvailabilityDTO> getAvailabilityPagination(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<CareerCycleAvailabilityEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertToDTO);
    }

    public CareerCycleAvailabilityDTO insertAvailability(CareerCycleAvailabilityDTO data) {
        if (data == null){
            throw new IllegalArgumentException("Datos no correctoss");
        }
        try {
            CareerCycleAvailabilityEntity entity = convertirAEntity(data);
            CareerCycleAvailabilityEntity registroGuardado = repo.save(entity);
            return convertToDTO(registroGuardado);
        } catch (Exception e) {
            log.error("Error al querer guardar los datos ingresados" + e.getMessage());
            throw new IllegalArgumentException("Error al registrar el nuevo dato");
        }
    }

    private CareerCycleAvailabilityEntity convertirAEntity(CareerCycleAvailabilityDTO data) {
        CareerCycleAvailabilityEntity entity = new CareerCycleAvailabilityEntity();
        if(data.getYearCycleId() != null){
            YearCyclesEntity yearCycles = yearCyclesRepo.findById(data.getYearCycleId())
                    .orElseThrow(() -> new IllegalArgumentException("ciclo no encontrado con ID: " + data.getYearCycleId()));
            entity.setYearCycles(yearCycles);
        }
        if(data.getCareerId() != null){
            CareerEntity career = careerRepository.findById(data.getCareerId())
                    .orElseThrow(() -> new IllegalArgumentException("Carrera no encontrada con ID: " + data.getCareerId()));
            entity.setCareer(career);
        }
        return entity;
    }

    public CareerCycleAvailabilityDTO updateAvailability(String id, CareerCycleAvailabilityDTO dto) {
        CareerCycleAvailabilityEntity entity = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Disponibilidad no encontrada"));
        if(dto.getCareerId() != null){
            CareerEntity career = careerRepository.findById(dto.getCareerId())
                    .orElseThrow(() -> new IllegalArgumentException("carrera no encontrada con ID: " + dto.getCareerId()));
            entity.setCareer(career);
        }else {
            entity.setCareer(null);
        }
        if(dto.getYearCycleId() != null){
            YearCyclesEntity yearCycles = yearCyclesRepo.findById(dto.getYearCycleId())
                    .orElseThrow(() -> new IllegalArgumentException("ciclo no encontrado con ID: " + dto.getYearCycleId()));
            entity.setYearCycles(yearCycles);
        }else {
            entity.setYearCycles(null);
        }

        return convertToDTO(repo.save(entity));
    }

    public boolean deleteAvailability(String id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }

    private CareerCycleAvailabilityDTO convertToDTO(CareerCycleAvailabilityEntity entity) {
        CareerCycleAvailabilityDTO dto = new CareerCycleAvailabilityDTO();
        dto.setId(entity.getId());
        if(entity.getCareer() != null){
            dto.setCareer(entity.getCareer().getCareerCode());
            dto.setCareerId(entity.getCareer().getId());

        }else {
            dto.setCareer("Sin Carrera Asignada");
            dto.setCareerId(null);
        }
        if(entity.getYearCycles() != null){
            dto.setYearCycleId(entity.getYearCycles().getId());
        }else {
            dto.setYearCycle("Sin ciclo Asignado");
            dto.setYearCycleId(null);
        }
        return dto;
    }
}
