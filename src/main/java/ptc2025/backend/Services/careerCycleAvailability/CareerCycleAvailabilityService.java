package ptc2025.backend.Services.careerCycleAvailability;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.careerCycleAvailability.CareerCycleAvailabilityEntity;
import ptc2025.backend.Entities.careers.CareerEntity;
import ptc2025.backend.Models.DTO.careerCycleAvailability.CareerCycleAvailabilityDTO;
import ptc2025.backend.Respositories.careerCycleAvailability.CareerCycleAvailabilityRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CareerCycleAvailabilityService {

    @Autowired
    private CareerCycleAvailabilityRepository repo;

    @Autowired
    private ptc2025.backend.Respositories.careers.CareerRepository careerRepository;

    public List<CareerCycleAvailabilityDTO> getAvailability() {
        return repo.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CareerCycleAvailabilityDTO insertAvailability(CareerCycleAvailabilityDTO dto) {
        CareerEntity career = careerRepository.findById(dto.getCareerId())
                .orElseThrow(() -> new IllegalArgumentException("Carrera no encontrada"));

        CareerCycleAvailabilityEntity entity = new CareerCycleAvailabilityEntity();
        entity.setId(dto.getId());
        entity.setYearCycleId(dto.getYearCycleId());
        entity.setCareer(career);

        return convertToDTO(repo.save(entity));
    }

    public CareerCycleAvailabilityDTO updateAvailability(String id, CareerCycleAvailabilityDTO dto) {
        CareerCycleAvailabilityEntity entity = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Disponibilidad no encontrada"));

        CareerEntity career = careerRepository.findById(dto.getCareerId())
                .orElseThrow(() -> new IllegalArgumentException("Carrera no encontrada"));

        entity.setYearCycleId(dto.getYearCycleId());
        entity.setCareer(career);

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
        dto.setYearCycleId(entity.getYearCycleId());
        dto.setCareerId(entity.getCareer().getId());
        return dto;
    }
}
