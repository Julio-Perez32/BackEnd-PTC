package ptc2025.backend.Services.Students;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ptc2025.backend.Entities.StudentCareerEnrollments.StudentCareerEnrollmentsEntity;
import ptc2025.backend.Entities.Students.StudentsEntity;
import ptc2025.backend.Entities.cyclicStudentPerformances.CyclicStudentPerformanceEntity;
import ptc2025.backend.Entities.studentCycleEnrollments.StudentCycleEnrollmentEntity;
import ptc2025.backend.Models.DTO.Students.StudentsDTO;
import ptc2025.backend.Respositories.People.PeopleRepository;
import ptc2025.backend.Respositories.StudentCareerEnrollments.StudentCareerEnrollmentsRepository;
import ptc2025.backend.Respositories.Students.StudentsRepository;
import ptc2025.backend.Respositories.YearCycles.YearCyclesRepository;
import ptc2025.backend.Respositories.careers.CareerRepository;
import ptc2025.backend.Respositories.cyclicStudentPerformaces.CyclicStudentPerformanceRepository;
import ptc2025.backend.Respositories.studentCycleEnrollments.StudentCycleEnrollmentRepository;

@Service
@Slf4j
public class StudentCascadeService {

    @Autowired private StudentsRepository studentsRepo;
    @Autowired private PeopleRepository peopleRepo;
    @Autowired
    private CareerRepository careerRepo;
    @Autowired private YearCyclesRepository yearCyclesRepo;

    @Autowired private StudentCareerEnrollmentsRepository careerEnrollmentsRepo;
    @Autowired private StudentCycleEnrollmentRepository cycleEnrollmentsRepo;
    @Autowired private CyclicStudentPerformanceRepository performanceRepo;

    @Transactional
    public StudentsDTO registerStudentCascade(StudentsDTO dto) {
        //Validar persona
        var person = peopleRepo.findById(dto.getPersonID())
                .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada"));

        if (!"STUDENT".equalsIgnoreCase(person.getPersonTypes().getPersonType())) {
            throw new IllegalArgumentException("La persona a ingresar tiene que ser un estudiante");
        }

        //Verificar duplicados
        if (studentsRepo.existsByPeople(person)) {
            throw new IllegalArgumentException("Ya existe un estudiante para esta persona");
        }

        //Insertar student
        StudentsEntity student = new StudentsEntity();
        student.setStudentCode(dto.getStudentCode());
        student.setPeople(person);
        student = studentsRepo.save(student);

        //Insertar StudentCareerEnrollment
        var career = careerRepo.findById(dto.getCareerId())
                .orElseThrow(() -> new IllegalArgumentException("Carrera no encontrada"));
        StudentCareerEnrollmentsEntity careerEnrollment = new StudentCareerEnrollmentsEntity();
        careerEnrollment.setStudent(student);
        careerEnrollment.setCareer(career);
        careerEnrollment.setStatus("Activo");
        careerEnrollment = careerEnrollmentsRepo.save(careerEnrollment);

        //Insertar StudentCycleEnrollment
        var cycle = yearCyclesRepo.findById(dto.getYearCycleId())
                .orElseThrow(() -> new IllegalArgumentException("Ciclo académico no encontrado"));
        StudentCycleEnrollmentEntity cycleEnrollment = new StudentCycleEnrollmentEntity();
        cycleEnrollment.setStudentCareerEnrollment(careerEnrollment);
        cycleEnrollment.setYearCycles(cycle);
        cycleEnrollment.setStatus("Inscrito");
        cycleEnrollment.setRegisteredAt(java.time.LocalDate.now());
        cycleEnrollment = cycleEnrollmentsRepo.save(cycleEnrollment);

        //Insertar CyclicStudentPerformance
        CyclicStudentPerformanceEntity performance = new CyclicStudentPerformanceEntity();
        performance.setPerformanceID(java.util.UUID.randomUUID().toString()); // porque no usas @GeneratedValue
        performance.setStudentCycleEnrollment(cycleEnrollment);
        performance.setTotalValueUnits(0);
        performance.setTotalMeritUnit(0);
        performance.setMeritUnitCoefficient(0.0);
        performance.setComputedAt(java.time.LocalDate.now());
        performance = performanceRepo.save(performance);

        //Retornar DTO
        StudentsDTO response = new StudentsDTO();
        response.setStudentID(student.getStudentID());
        response.setStudentCode(student.getStudentCode());
        response.setPersonID(person.getPersonID());
        response.setCareerEnrollmentID(careerEnrollment.getStudentCareerEnrollmentID());
        response.setCycleEnrollmentID(cycleEnrollment.getId());
        response.setPerformanceID(performance.getPerformanceID());
        return response;
    }
}

