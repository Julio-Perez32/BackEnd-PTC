package ptc2025.backend.Controller.careers;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Entities.careers.CareerEntity;
import ptc2025.backend.Services.careers.CareerService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/careers")
@CrossOrigin
public class CareerController {

    private final CareerService careerService;

    public CareerController(CareerService careerService) {
        this.careerService = careerService;
    }

    @GetMapping
    public List<CareerEntity> getAll() {
        return careerService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<CareerEntity> getById(@PathVariable Long id) {
        return careerService.findById(id);
    }

    @PostMapping
    public CareerEntity create(@Valid @RequestBody CareerEntity career) {
        return careerService.save(career);
    }

    @PutMapping("/{id}")
    public CareerEntity update(@PathVariable Long id, @Valid @RequestBody CareerEntity career) {
        return careerService.update(id, career);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        careerService.delete(id);
    }
}
