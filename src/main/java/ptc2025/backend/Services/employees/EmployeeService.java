package ptc2025.backend.Services.employees;

import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.employees.EmployeeEntity;
import ptc2025.backend.Respositories.employees.EmployeeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<EmployeeEntity> findAll() {
        return employeeRepository.findAll();
    }

    public Optional<EmployeeEntity> findById(Long id) {
        return employeeRepository.findById(id);
    }

    public EmployeeEntity save(EmployeeEntity employeeEntity) {
        return employeeRepository.save(employeeEntity);
    }

    public EmployeeEntity update(Long id, EmployeeEntity newEmployee) {
        return employeeRepository.findById(id)
                .map(employee -> {
                    employee.setFirstName(newEmployee.getFirstName());
                    employee.setLastName(newEmployee.getLastName());
                    employee.setPosition(newEmployee.getPosition());
                    employee.setEmail(newEmployee.getEmail());
                    return employeeRepository.save(employee);
                }).orElse(null);
    }

    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }
}
