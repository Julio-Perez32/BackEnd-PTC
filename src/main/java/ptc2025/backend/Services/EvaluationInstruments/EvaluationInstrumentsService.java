package ptc2025.backend.Services.EvaluationInstruments;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.EvaluationInstruments.EvaluationInstrumentsEntity;
import ptc2025.backend.Models.DTO.EvaluationInstruments.EvaluationInstrumentsDTO;
import ptc2025.backend.Respositories.EvaluationInstruments.EvaluationInstrumentsRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EvaluationInstrumentsService {

    @Autowired
    EvaluationInstrumentsRepository repo;

    public List<EvaluationInstrumentsDTO> getAllEvaluations(){
        List<EvaluationInstrumentsEntity> evaluaciones = repo.findAll();
        return evaluaciones.stream()
                .map(this::convertToEvaluationDTO)
                .collect(Collectors.toList());
    }

    public Page<EvaluationInstrumentsDTO> getEvaluationInstrumentPagination(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<EvaluationInstrumentsEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertToEvaluationDTO);
    }

    private EvaluationInstrumentsDTO convertToEvaluationDTO(EvaluationInstrumentsEntity instrumentos) {
        EvaluationInstrumentsDTO dto = new EvaluationInstrumentsDTO();
        dto.setInstrumentID(instrumentos.getInstrumentID());
        dto.setInstrumentTypeID(instrumentos.getInstrumentTypeID());
        dto.setDescription(instrumentos.getDescription());
        dto.setUsesRubric(instrumentos.getUsesRubric());
        return dto;
    }

    private EvaluationInstrumentsEntity convertToEvaluationEntity(EvaluationInstrumentsDTO dto){
        EvaluationInstrumentsEntity entity = new EvaluationInstrumentsEntity();
        entity.setInstrumentID(dto.getInstrumentID());
        entity.setInstrumentTypeID(dto.getInstrumentTypeID());
        entity.setDescription(dto.getDescription());
        entity.setUsesRubric(dto.getUsesRubric());
        return entity;
    }

    public EvaluationInstrumentsDTO insertEvaluation(EvaluationInstrumentsDTO dto){
        if (dto == null || dto.getInstrumentTypeID() == null || dto.getInstrumentTypeID().isEmpty() ||
                dto.getDescription() == null || dto.getDescription().isEmpty() || dto.getUsesRubric() == null){
            throw new IllegalArgumentException("Todos los campos deben de estar llenados");
        }
        try{
            EvaluationInstrumentsEntity entity = convertToEvaluationEntity(dto);
            EvaluationInstrumentsEntity evaluationSaved = repo.save(entity);
            return convertToEvaluationDTO(evaluationSaved);
        }catch (Exception e){
            log.error("Error al registrar la evaluación: " + e.getMessage());
            throw new IllegalArgumentException("Error al registrar la evaluación");
        }
    }

    public EvaluationInstrumentsDTO updateEvaluation(String id, EvaluationInstrumentsDTO json){
        EvaluationInstrumentsEntity existsEvualution = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Evaluación no encontrada."));
        existsEvualution.setInstrumentTypeID(json.getInstrumentTypeID());
        existsEvualution.setDescription(json.getDescription());
        existsEvualution.setUsesRubric(json.getUsesRubric());

        EvaluationInstrumentsEntity evaluationUpdated = repo.save(existsEvualution);

        return convertToEvaluationDTO(evaluationUpdated);
    }

    public boolean deleteEvaluation(String id) {
        try {
            EvaluationInstrumentsEntity evaluationExists = repo.findById(id).orElse(null);
            if (evaluationExists != null) {
                repo.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("No se encontró una Evaluación con ID " + id + " para eliminar", 1);
        }
    }
}
