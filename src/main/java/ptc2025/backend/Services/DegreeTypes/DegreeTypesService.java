package ptc2025.backend.Services.DegreeTypes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.DegreeTypes.DegreeTypesEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Exceptions.ExceptionBadRequest;
import ptc2025.backend.Exceptions.ExceptionNotFound;
import ptc2025.backend.Exceptions.ExceptionServerError;
import ptc2025.backend.Models.DTO.DegreeTypes.DegreeTypesDTO;
import ptc2025.backend.Respositories.DegreeTypes.DegreeTypesRepository;
import ptc2025.backend.Respositories.Universities.UniversityRespository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DegreeTypesService {

    @Autowired
    private DegreeTypesRepository repo;

    @Autowired
    UniversityRespository repoUniversity;

    public List<DegreeTypesDTO> getAllDegreeTypes() {
        List<DegreeTypesEntity> degreetype = repo.findAll();
        return degreetype.stream()
                .map(this::hacerdegreestypeADTO)
                .collect(Collectors.toList());
    }

    // Paginación
    public Page<DegreeTypesDTO> getAllDegreeTypesPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DegreeTypesEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::hacerdegreestypeADTO);
    }

    public DegreeTypesDTO insertarDatos(DegreeTypesDTO data) {
        if (data == null || data.getUniversityID() == null || data.getUniversityID().isBlank()
                || data.getDegreeTypeName() == null || data.getDegreeTypeName().isBlank()) {
            throw new ExceptionBadRequest("Todos los campos son obligatorios para registrar un DegreeType");
        }
        try {
            DegreeTypesEntity entity = convertirAEntity(data);
            DegreeTypesEntity degreeTypeGuardado = repo.save(entity);
            return hacerdegreestypeADTO(degreeTypeGuardado);
        } catch (Exception e) {
            log.error("Error al registrar el tipo de título: {}", e.getMessage());
            throw new ExceptionServerError("Error interno al registrar tipo de título: " + e.getMessage());
        }
    }

    private DegreeTypesEntity convertirAEntity(DegreeTypesDTO data) {
        DegreeTypesEntity entity = new DegreeTypesEntity();
        entity.setDegreeTypeName(data.getDegreeTypeName());

        if (data.getUniversityID() != null) {
            UniversityEntity university = repoUniversity.findById(data.getUniversityID())
                    .orElseThrow(() -> new ExceptionNotFound("Universidad no encontrada con ID: " + data.getUniversityID()));
            entity.setUniversity(university);
        }
        return entity;
    }

    public DegreeTypesDTO hacerdegreestypeADTO(DegreeTypesEntity degreeT) {
        DegreeTypesDTO dto = new DegreeTypesDTO();
        dto.setId(degreeT.getId());
        dto.setDegreeTypeName(degreeT.getDegreeTypeName());

        if (degreeT.getUniversity() != null) {
            dto.setUniversityName(degreeT.getUniversity().getUniversityName());
            dto.setUniversityID(degreeT.getUniversity().getUniversityID());
        } else {
            dto.setUniversityName("Sin Universidad Asignada");
            dto.setUniversityID(null);
        }
        return dto;
    }

    public DegreeTypesDTO actualizarDegreeType(String id, DegreeTypesDTO json) {
        DegreeTypesEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionNotFound("Tipo de título no encontrado con ID: " + id));

        existente.setDegreeTypeName(json.getDegreeTypeName());

        if (json.getUniversityID() != null) {
            UniversityEntity university = repoUniversity.findById(json.getUniversityID())
                    .orElseThrow(() -> new ExceptionNotFound("Universidad no encontrada con ID: " + json.getUniversityID()));
            existente.setUniversity(university);
        } else {
            existente.setUniversity(null);
        }

        DegreeTypesEntity degreeTypeAtualizado = repo.save(existente);
        return hacerdegreestypeADTO(degreeTypeAtualizado);
    }

    public boolean eliminarDegreeType(String id) {
        try {
            DegreeTypesEntity existente = repo.findById(id)
                    .orElseThrow(() -> new ExceptionNotFound("Tipo de título no encontrado con ID: " + id));

            repo.deleteById(id);
            return true;

        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionServerError("Error al intentar eliminar el tipo de título: " + e.getMessage());
        }
    }
}
