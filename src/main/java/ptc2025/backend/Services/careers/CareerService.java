package ptc2025.backend.Services.careers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.careers.CareerEntity;
import ptc2025.backend.Models.DTO.careers.CareerDTO;
import ptc2025.backend.Respositories.careers.CareerRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CareerService {

    @Autowired
    private CareerRepository repo;

    // GET
    public List<CareerDTO> getCareers() {
        return repo.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // POST
    public CareerDTO insertCareer(CareerDTO dto) {
        if (dto == null) throw new IllegalArgumentException("Carrera no puede ser nula");
        if (repo.existsById(dto.getId())) throw new IllegalArgumentException("La carrera ya existe");
        CareerEntity entity = convertirAEntity(dto);
        CareerEntity saved = repo.save(entity);
        return convertirADTO(saved);
    }

    // PUT
    public CareerDTO updateCareer(String id, CareerDTO dto) {
        try {
            if (repo.existsById(id)) {
                CareerEntity entity = repo.getById(id);
                entity.setName(dto.getName());
                entity.setDescription(dto.getDescription());
                entity.setFacultyId(dto.getFacultyId());
                entity.setIsActive(dto.getIsActive());
                CareerEntity saved = repo.save(entity);
                return convertirADTO(saved);
            }
            throw new IllegalArgumentException("La carrera con ID " + id + " no pudo ser actualizada");
        } catch (Exception e) {
            throw new RuntimeException("No se pudo actualizar la carrera: " + e.getMessage());
        }
    }

    // DELETE
    public boolean deleteCareer(String id) {
        try {
            if (repo.existsById(id)) {
                repo.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("La carrera con ID " + id + " no existe", 1);
        }
    }

    // CONVERSIONES
    private CareerDTO convertirADTO(CareerEntity entity) {
        CareerDTO dto = new CareerDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setFacultyId(entity.getFacultyId());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }

    private CareerEntity convertirAEntity(CareerDTO dto) {
        CareerEntity entity = new CareerEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setFacultyId(dto.getFacultyId());
        entity.setIsActive(dto.getIsActive());
        return entity;
    }
}
