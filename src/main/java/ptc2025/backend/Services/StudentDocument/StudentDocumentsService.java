package ptc2025.backend.Services.StudentDocument;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ptc2025.backend.Entities.Documents.DocumentEntity;
import ptc2025.backend.Entities.StudentDocument.StudentDocumentsEntity;
import ptc2025.backend.Entities.Students.StudentsEntity;
import ptc2025.backend.Exceptions.ExceptionNotFound;
import ptc2025.backend.Exceptions.ExceptionServerError;
import ptc2025.backend.Models.DTO.StudentDocument.StudentDocumentsDTO;
import ptc2025.backend.Respositories.Documents.DocumentRepository;
import ptc2025.backend.Respositories.StudentDocument.StudentDocumentsRepository;
import ptc2025.backend.Respositories.Students.StudentsRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StudentDocumentsService {
    @Autowired
    StudentDocumentsRepository repo;

    @Autowired
    StudentsRepository repoStudents;

    @Autowired
    DocumentRepository repoDocuments;

    // GET
    public List<StudentDocumentsDTO> getStudentDocument() {
        try {
            List<StudentDocumentsEntity> documents = repo.findAll();
            return documents.stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ExceptionServerError("Error al obtener los documentos de los estudiantes: " + e.getMessage());
        }
    }

    public Page<StudentDocumentsDTO> getStudentDocumentPagination(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<StudentDocumentsEntity> pageEntity = repo.findAll(pageable);
            return pageEntity.map(this::convertirADTO);
        } catch (Exception e) {
            throw new ExceptionServerError("Error al paginar los documentos de los estudiantes: " + e.getMessage());
        }
    }

    // POST
    public StudentDocumentsDTO insertStudentDocuments(@RequestBody StudentDocumentsDTO dto) {
        if (dto == null || dto.getStudentID() == null || dto.getStudentID().isBlank()
                || dto.getDocumentID() == null || dto.getDocumentID().isBlank()) {
            throw new IllegalArgumentException("Todos los campos son obligatorios");
        }
        if ((dto.getSubmitted() != null && !(dto.getSubmitted() == 'Y' || dto.getSubmitted() == 'N')) ||
                (dto.getVerified() != null && !(dto.getVerified() == 'Y' || dto.getVerified() == 'N'))) {
            throw new IllegalArgumentException("Enviado y Verificado solo pueden ser 'Y' o 'N'");
        }

        try {
            StudentDocumentsEntity entity = convertirAEntity(dto);
            StudentDocumentsEntity guardardo = repo.save(entity);
            return convertirADTO(guardardo);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al guardar los documentos del estudiante: " + e.getMessage());
            throw new ExceptionServerError("Error interno al registrar los documentos del estudiante: " + e.getMessage());
        }
    }

    // PUT
    public StudentDocumentsDTO updateStudentDocument(String id, StudentDocumentsDTO dto) {
        try {
            StudentDocumentsEntity existente = repo.findById(id)
                    .orElseThrow(() -> new ExceptionNotFound("No se encontró documentos con ID: " + id));

            existente.setSubmitted(dto.getSubmitted());
            existente.setSubmissionDate(dto.getSubmissionDate());
            existente.setVerified(dto.getVerified());

            if (dto.getStudentID() != null) {
                StudentsEntity student = repoStudents.findById(dto.getStudentID())
                        .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado con ID " + dto.getStudentID()));
                existente.setStudents(student);
            } else {
                existente.setStudents(null);
            }

            if (dto.getDocumentID() != null) {
                DocumentEntity document = repoDocuments.findById(dto.getDocumentID())
                        .orElseThrow(() -> new IllegalArgumentException("Documento no encontrado con ID " + dto.getDocumentID()));
                existente.setDocument(document);
            } else {
                existente.setDocument(null);
            }

            StudentDocumentsEntity actu = repo.save(existente);
            return convertirADTO(actu);
        } catch (IllegalArgumentException | ExceptionNotFound e) {
            throw e;
        } catch (Exception e) {
            throw new ExceptionServerError("Error al actualizar los documentos: " + e.getMessage());
        }
    }

    // DELETE
    public boolean deleteStudentDocument(String id) {
        try {
            StudentDocumentsEntity objCompo = repo.findById(id).orElse(null);
            if (objCompo != null) {
                repo.deleteById(id);
                return true;
            } else {
                throw new ExceptionNotFound("Documentos del estudiante no encontrados con ID: " + id);
            }
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionNotFound("No se encontró ningún documento del estudiante con el ID: " + id + " para poder ser eliminado");
        } catch (Exception e) {
            throw new ExceptionServerError("Error al eliminar documentos: " + e.getMessage());
        }
    }

    // CONVERSIÓN ENTITY -> DTO
    private StudentDocumentsDTO convertirADTO(StudentDocumentsEntity entity) {
        StudentDocumentsDTO dto = new StudentDocumentsDTO();
        dto.setStudentDocumentID(entity.getStudentDocumentID());
        dto.setSubmitted(entity.getSubmitted());
        dto.setSubmissionDate(entity.getSubmissionDate());
        dto.setVerified(entity.getVerified());

        if (entity.getStudents() != null) {
            dto.setStudent(entity.getStudents().getStudentID());
        } else {
            dto.setStudent("Sin estudiante asignado");
            dto.setStudentID(null);
        }

        if (entity.getDocument() != null) {
            dto.setDocument(entity.getDocument().getId());
        } else {
            dto.setDocument("Sin documento asignado");
            dto.setDocumentID(null);
        }
        return dto;
    }

    // CONVERSIÓN DTO -> ENTITY
    private StudentDocumentsEntity convertirAEntity(StudentDocumentsDTO dto) {
        StudentDocumentsEntity entity = new StudentDocumentsEntity();
        entity.setSubmitted(dto.getSubmitted());
        entity.setSubmissionDate(dto.getSubmissionDate());
        entity.setVerified(dto.getVerified());

        if (dto.getStudentID() != null) {
            StudentsEntity student = repoStudents.findById(dto.getStudentID())
                    .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado con ID " + dto.getStudentID()));
            entity.setStudents(student);
        }

        if (dto.getDocumentID() != null) {
            DocumentEntity document = repoDocuments.findById(dto.getDocumentID())
                    .orElseThrow(() -> new IllegalArgumentException("Documento no encontrado con ID " + dto.getDocumentID()));
            entity.setDocument(document);
        }
        return entity;
    }
}
