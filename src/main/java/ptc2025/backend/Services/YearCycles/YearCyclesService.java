package ptc2025.backend.Services.YearCycles;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import ptc2025.backend.Entities.AcademicYear.AcademicYearEntity;
import ptc2025.backend.Entities.CycleTypes.CycleTypesEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Entities.YearCycles.YearCyclesEntity;
import ptc2025.backend.Models.DTO.YearCycles.YearCyclesDTO;
import ptc2025.backend.Respositories.AcademicYear.AcademicYearRepository;
import ptc2025.backend.Respositories.CycleTypes.CycleTypesRepository;
import ptc2025.backend.Respositories.YearCycles.YearCyclesRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@CrossOrigin
public class YearCyclesService {

    @Autowired
    YearCyclesRepository repo;

    @Autowired
    AcademicYearRepository academicYearRepo;

    @Autowired
    CycleTypesRepository cycleTypesRepo;

    public List<YearCyclesDTO> obtenerRegistros() {
        List<YearCyclesEntity> Lista = repo.findAll();
        return Lista.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private YearCyclesDTO convertirADTO(YearCyclesEntity yearCyclesEntity) {
        YearCyclesDTO dto = new YearCyclesDTO();
        dto.setId(yearCyclesEntity.getId());
        dto.setStartDate(yearCyclesEntity.getStartDate());
        dto.setEndDate(yearCyclesEntity.getEndDate());
        if(yearCyclesEntity.getAcademicYear() != null){
            dto.setAcademicyear(yearCyclesEntity.getAcademicYear().getAcademicYearId());
        }else {
            dto.setAcademicyear("Sin año academico Asignado");
            dto.setAcademicyear(null);
        }
        if(yearCyclesEntity.getCycleTypes() != null){
            dto.setCycletype(yearCyclesEntity.getCycleTypes().getCycleLabel());
            dto.setCycletype(yearCyclesEntity.getCycleTypes().getId());
        }else {
            dto.setCycletype("Sin tipo de año Asignado");
            dto.setCycletype(null);
        }
        return dto;
    }

    public YearCyclesDTO insertarDatos(YearCyclesDTO data) {
        if (data == null){
            throw new IllegalArgumentException("Datos no correctoss");
        }
        try {
            YearCyclesEntity entity = convertirAEntity(data);
            YearCyclesEntity registroGuardado = repo.save(entity);
            return convertirADTO(registroGuardado);
        } catch (Exception e) {
            log.error("Error al querer guardar los datos ingresados" + e.getMessage());
            throw new IllegalArgumentException("Error al registrar el nuevo dato");
        }
    }

    private YearCyclesEntity convertirAEntity(YearCyclesDTO data) {
        YearCyclesEntity entity = new YearCyclesEntity();
        entity.setStartDate(data.getStartDate());
        entity.setEndDate(data.getEndDate());
        if(data.getAcademicYearID() != null){
            AcademicYearEntity academicYear = academicYearRepo.findById(data.getAcademicYearID())
                    .orElseThrow(() -> new IllegalArgumentException("año academico no encontrado con ID: " + data.getAcademicYearID()));
            entity.setAcademicYear(academicYear);
        }
        if(data.getCycleTypeID() != null){
            CycleTypesEntity cycleTypes = cycleTypesRepo.findById(data.getCycleTypeID())
                    .orElseThrow(() -> new IllegalArgumentException("Tipo de ciclo no encontrado con ID: " + data.getCycleTypeID()));
            entity.setCycleTypes(cycleTypes);
        }
        return entity;
    }

    public YearCyclesDTO ActualizarRegistro(String id, YearCyclesDTO json) {
        YearCyclesEntity existente = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Registro no encontrado"));
        existente.setStartDate(json.getStartDate());
        existente.setEndDate(json.getEndDate());
        if(json.getAcademicYearID() != null){
            AcademicYearEntity academicYear = academicYearRepo.findById(json.getCycleTypeID())
                    .orElseThrow(() -> new IllegalArgumentException("Año academico no encontrado con ID: " + json.getCycleTypeID()));
            existente.setAcademicYear(academicYear);
        }else {
            existente.setAcademicYear(null);
        }
        if(json.getCycletype() != null){
            CycleTypesEntity cycleTypes = cycleTypesRepo.findById(json.getCycleTypeID())
                    .orElseThrow(() -> new IllegalArgumentException("Tipo de año no encontrado con ID: " + json.getCycleTypeID()));
            existente.setCycleTypes(cycleTypes);
        }else {
            existente.setCycleTypes(null);
        }
        YearCyclesEntity RegistroActualizado = repo.save(existente);
        return convertirADTO(RegistroActualizado);
    }

    public boolean removerRegistro(String id) {
        try{
            YearCyclesEntity existente = repo.findById(id).orElse(null);
            if (existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro el  registro", 1);
        }
    }

        //paginacion no entiendo jeje
    public Page<YearCyclesDTO> getAllCategories(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<YearCyclesEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirADTO);
    }
}
