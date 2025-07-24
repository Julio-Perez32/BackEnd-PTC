package ptc2025.backend.Services.careers;

import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.careers.CareerEntity;
import ptc2025.backend.Respositories.careers.CareerRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CareerService {

    private final CareerRepository careerRepository;

    public CareerService(CareerRepository careerRepository) {
        this.careerRepository = careerRepository;
    }

    public List<CareerEntity> findAll() {
        return careerRepository.findAll();
    }

    public Optional<CareerEntity> findById(Long id) {
        return careerRepository.findById(id);
    }

    public CareerEntity save(CareerEntity career) {
        return careerRepository.save(career);
    }

    public CareerEntity update(Long id, CareerEntity newCareer) {
        return careerRepository.findById(id)
                .map(career -> {
                    career.setName(newCareer.getName());
                    career.setDescription(newCareer.getDescription());
                    return careerRepository.save(career);
                }).orElse(null);
    }

    public void delete(Long id) {
        careerRepository.deleteById(id);
    }
}
