package ptc2025.backend.Controller.employees;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Entities.employees.EmployeeEntity;
import ptc2025.backend.Respositories.employees.EmployeeRepository;
import ptc2025.backend.Services.employees.EmployeeService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<EmployeeEntity> getAll() {
        return employeeService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<EmployeeEntity> getById(@PathVariable Long id) {
        return employeeService.findById(id);
    }

    @PostMapping
    public EmployeeEntity create(@Valid @RequestBody EmployeeEntity employee) {
        return employeeService.save(employee);
    }

    @PutMapping("/{id}")
    public EmployeeEntity update(@PathVariable Long id, @Valid @RequestBody EmployeeEntity employee) {
        return employeeService.update(id, employee);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        employeeService.delete(id);
    }
}
