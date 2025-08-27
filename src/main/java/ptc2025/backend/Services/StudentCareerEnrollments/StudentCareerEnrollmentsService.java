package ptc2025.backend.Services.StudentCareerEnrollments;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.SocialServiceProjects.SocialServiceProjectsEntity;
import ptc2025.backend.Entities.StudentCareerEnrollments.StudentCareerEnrollmentsEntity;
import ptc2025.backend.Entities.Students.StudentsEntity;
import ptc2025.backend.Entities.careerSocialServiceProjects.CareerSocialServiceProjectEntity;
import ptc2025.backend.Models.DTO.StudentCareerEnrollments.StudentCareerEnrollmentsDTO;
import ptc2025.backend.Respositories.SocialService.SocialServiceRespository;
import ptc2025.backend.Respositories.StudentCareerEnrollments.StudentCareerEnrollmentsRepository;
import ptc2025.backend.Respositories.Students.StudentsRepository;
import ptc2025.backend.Respositories.careerSocialServiceProjects.CareerSocialServiceProjectRepository;
import ptc2025.backend.Respositories.careers.CareerRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StudentCareerEnrollmentsService {

    @Autowired
    private StudentCareerEnrollmentsRepository repo;

    @Autowired
    private CareerRepository careerRepo;

    @Autowired
    private StudentsRepository studentsRepository;

    @Autowired
    SocialServiceRespository repoSocialService;

    @Autowired
    CareerSocialServiceProjectRepository repoCareerSocial;

    // GET
    public List<StudentCareerEnrollmentsDTO> getAllEnrollments(){
        return repo.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // INSERT
    public StudentCareerEnrollmentsDTO insertEnrollment(StudentCareerEnrollmentsDTO dto){
        StudentCareerEnrollmentsEntity entity = convertToEntity(dto);
        StudentCareerEnrollmentsEntity saved = repo.save(entity);
        return convertToDTO(saved);
    }

    // UPDATE
    public StudentCareerEnrollmentsDTO updateEnrollment(String id, StudentCareerEnrollmentsDTO dto){
        StudentCareerEnrollmentsEntity exists = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El ID no fue encontrado"));

        exists.setStartDate(dto.getStartDate());
        exists.setEndDate(dto.getEndDate());
        exists.setStatus(dto.getStatus());
        exists.setStatusDate(dto.getStatusDate());
        exists.setServiceStartDate(dto.getServiceStartDate());
        exists.setServiceEndDate(dto.getServiceEndDate());
        exists.setServiceStatus(dto.getServiceStatus());
        exists.setServiceStatusDate(dto.getServiceStatusDate());

        if(dto.getCareerID() != null){
            ptc2025.backend.Entities.careers.CareerEntity career = careerRepo.findById(dto.getCareerID())
                    .orElseThrow(() -> new IllegalArgumentException("Carrera no encontrada con ID: " + dto.getCareerID()));
            exists.setCareer(career);
        }
        if(dto.getStudentID() != null){
            StudentsEntity student = studentsRepository.findById(dto.getStudentID())
                    .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado con ID: " + dto.getStudentID()));
            exists.setStudent(student);
        }
        if(dto.getSocialServiceProjectID() != null){
            SocialServiceProjectsEntity project = repoSocialService.findById(dto.getSocialServiceProjectID()).orElseThrow(
                    () -> new IllegalArgumentException("Proyecto no encontrado con ID: " + dto.getSocialServiceProjectID()));
            exists.setSocialServiceProject(project);
        }

        return convertToDTO(repo.save(exists));
    }

    // DELETE
    public boolean deleteEnrollment(String id){
        try {
            if(repo.existsById(id)){
                repo.deleteById(id);
                return true;
            }
            return false;
        } catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontró inscripción con ID: " + id, 1);
        }
    }

    private StudentCareerEnrollmentsDTO convertToDTO(StudentCareerEnrollmentsEntity entity){
        StudentCareerEnrollmentsDTO dto = new StudentCareerEnrollmentsDTO();
        dto.setStudentCareerEnrollmentID(entity.getStudentCareerEnrollmentID());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setStatus(entity.getStatus());
        dto.setStatusDate(entity.getStatusDate());
        dto.setServiceStartDate(entity.getServiceStartDate());
        dto.setServiceEndDate(entity.getServiceEndDate());
        dto.setServiceStatus(entity.getServiceStatus());
        dto.setServiceStatusDate(entity.getServiceStatusDate());

        if(entity.getCareer() != null){
            dto.setCareerID(entity.getCareer().getId());
            dto.setCareerName(entity.getCareer().getNameCareer());
        }
        if(entity.getStudent() != null){
            dto.setStudentName(entity.getStudent().getPeople().getFirstName());
            dto.setStudentID(entity.getStudent().getStudentID());
        }
        if(entity.getSocialServiceProject() != null){
            dto.setSocialServiceProjectName(entity.getSocialServiceProject().getSocialServiceProjectName());
            dto.setSocialServiceProjectID(entity.getSocialServiceProject().getSocialServiceProjectID());
        }
        return dto;
    }

    private StudentCareerEnrollmentsEntity convertToEntity(StudentCareerEnrollmentsDTO dto){
        StudentCareerEnrollmentsEntity entity = new StudentCareerEnrollmentsEntity();
        entity.setStudentCareerEnrollmentID(dto.getStudentCareerEnrollmentID());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setStatus(dto.getStatus());
        entity.setStatusDate(dto.getStatusDate());
        entity.setServiceStartDate(dto.getServiceStartDate());
        entity.setServiceEndDate(dto.getServiceEndDate());
        entity.setServiceStatus(dto.getServiceStatus());
        entity.setServiceStatusDate(dto.getServiceStatusDate());

        if(dto.getCareerID() != null){
            ptc2025.backend.Entities.careers.CareerEntity career = careerRepo.findById(dto.getCareerID())
                    .orElseThrow(() -> new IllegalArgumentException("Carrera no encontrada con ID: " + dto.getCareerID()));
            entity.setCareer(career);
        }
        if(dto.getStudentID() != null){
            StudentsEntity student = studentsRepository.findById(dto.getStudentID())
                    .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado con ID: " + dto.getStudentID()));
            entity.setStudent(student);
        }
        if(dto.getSocialServiceProjectID() != null){
            SocialServiceProjectsEntity project = repoSocialService.findById(dto.getSocialServiceProjectID()).orElseThrow(
                            () -> new IllegalArgumentException("Proyecto no encontrado con ID: " + dto.getSocialServiceProjectID()));
            entity.setSocialServiceProject(project);
        }

        return entity;
    }
}
