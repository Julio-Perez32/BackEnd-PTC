package ptc2025.backend.Services.YearCycles;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.AcademicYear.AcademicYearEntity;
import ptc2025.backend.Entities.CycleTypes.CycleTypesEntity;
import ptc2025.backend.Entities.Users.UsersEntity;
import ptc2025.backend.Entities.YearCycles.YearCyclesEntity;
import ptc2025.backend.Exceptions.ExceptionNoSuchElement;
import ptc2025.backend.Exceptions.ExceptionNotFound;
import ptc2025.backend.Models.DTO.Users.UsersDTO;
import ptc2025.backend.Models.DTO.YearCycles.YearCyclesDTO;
import ptc2025.backend.Respositories.AcademicYear.AcademicYearRepository;
import ptc2025.backend.Respositories.CycleTypes.CycleTypesRepository;
import ptc2025.backend.Respositories.YearCycles.YearCyclesRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class YearCyclesService {

    @Autowired
    YearCyclesRepository repo;

    @Autowired
    AcademicYearRepository academicYearRepo;

    @Autowired
    CycleTypesRepository cycleTypesRepo;

    public List<YearCyclesDTO> obtenerRegistros() {
        List<YearCyclesEntity> Lista = repo.findAll();
        if (Lista.isEmpty()) {
            throw new ExceptionNoSuchElement("No existen registros de YearCycles en la base de datos");
        }
        return Lista.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    public Page<YearCyclesDTO> getYearCyclesPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<YearCyclesEntity> pageEntity = repo.findAll(pageable);

        if (pageEntity.isEmpty()) {
            throw new ExceptionNotFound("No se encontraron definiciones de materias para la página solicitada.");
        }

        return pageEntity.map(this::convertirADTO);
    }

    private YearCyclesDTO convertirADTO(YearCyclesEntity e) {
        YearCyclesDTO dto = new YearCyclesDTO();
        dto.setId(e.getId());
        dto.setStartDate(e.getStartDate());
        dto.setEndDate(e.getEndDate());

        if (e.getACADEMICYEAR() != null) {
            dto.setAcademicYearID(e.getACADEMICYEAR().getAcademicYearId());
            dto.setAcademicyear(e.getACADEMICYEAR().getAcademicYearId());
        }
        if (e.getCycleTypes() != null) {
            dto.setCycleTypeID(e.getCycleTypes().getId());
            dto.setCycletype(e.getCycleTypes().getId());
        }
        return dto;
    }


    public YearCyclesDTO insertarDatos(YearCyclesDTO data) {
        if (data == null) {
            throw new IllegalArgumentException("Los datos proporcionados son nulos o inválidos");
        }
        try {
            YearCyclesEntity entity = convertirAEntity(data);
            YearCyclesEntity registroGuardado = repo.save(entity);
            return convertirADTO(registroGuardado);
        } catch (Exception e) {
            log.error("Error al querer guardar los datos ingresados: " + e.getMessage());
            throw new IllegalArgumentException("Error al registrar el nuevo dato");
        }
    }

    private YearCyclesEntity convertirAEntity(YearCyclesDTO data) {
        YearCyclesEntity entity = new YearCyclesEntity();
        entity.setStartDate(data.getStartDate());
        entity.setEndDate(data.getEndDate());

        if (data.getAcademicYearID() != null) {
            AcademicYearEntity academicYear = academicYearRepo.findById(data.getAcademicYearID())
                    .orElseThrow(() -> new ExceptionNoSuchElement(
                            "Año académico no encontrado con ID: " + data.getAcademicYearID()
                    ));
            entity.setACADEMICYEAR(academicYear);
        }

        if (data.getCycleTypeID() != null) {
            CycleTypesEntity cycleTypes = cycleTypesRepo.findById(data.getCycleTypeID())
                    .orElseThrow(() -> new ExceptionNoSuchElement(
                            "Tipo de ciclo no encontrado con ID: " + data.getCycleTypeID()
                    ));
            entity.setCycleTypes(cycleTypes);
        }

        return entity;
    }

    public YearCyclesDTO ActualizarRegistro(String id, YearCyclesDTO json) {
        YearCyclesEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionNoSuchElement("Registro no encontrado con ID: " + id));

        existente.setStartDate(json.getStartDate());
        existente.setEndDate(json.getEndDate());

        if (json.getAcademicYearID() != null) {
            AcademicYearEntity ay = academicYearRepo.findById(json.getAcademicYearID())
                    .orElseThrow(() -> new ExceptionNoSuchElement(
                            "Año académico no encontrado con ID: " + json.getAcademicYearID()));
            existente.setACADEMICYEAR(ay);
        } else {
            existente.setACADEMICYEAR(null);
        }

        if (json.getCycleTypeID() != null) {
            CycleTypesEntity ct = cycleTypesRepo.findById(json.getCycleTypeID())
                    .orElseThrow(() -> new ExceptionNoSuchElement(
                            "Tipo de ciclo no encontrado con ID: " + json.getCycleTypeID()));
            existente.setCycleTypes(ct);
        } else {
            existente.setCycleTypes(null);
        }

        YearCyclesEntity actualizado = repo.save(existente);
        return convertirADTO(actualizado);
    }

    public boolean removerRegistro(String id) {
        try {
            YearCyclesEntity existente = repo.findById(id)
                    .orElseThrow(() -> new ExceptionNoSuchElement("No se encontró el registro con ID: " + id));
            repo.deleteById(existente.getId());
            return true;
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionNoSuchElement("Error al intentar eliminar: el registro no existe con ID: " + id);
        }
    }

    // Paginación
    public Page<YearCyclesDTO> getAllCategories(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<YearCyclesEntity> pageEntity = repo.findAll(pageable);

        if (pageEntity.isEmpty()) {
            throw new ExceptionNoSuchElement("No existen registros para paginación en YearCycles");
        }
        return pageEntity.map(this::convertirADTO);
    }
}
