package ptc2025.backend.Services.Modalities;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.Modalities.ModalitiesEntity;
import ptc2025.backend.Models.DTO.Modalities.ModalitiesDTO;
import ptc2025.backend.Respositories.Modalities.ModalityRepository;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class ModalityService {

    @Autowired
    private ModalityRepository repo;

    public List<ModalitiesDTO> getAllModalities(){
        List<ModalitiesEntity> modalities = repo.findAll();
        return modalities.stream()
                .map(this::convertirModalidadesADTO)
                .collect(Collectors.toList());
    }

    public ModalitiesDTO convertirModalidadesADTO(ModalitiesEntity modalida){
        ModalitiesDTO dto = new ModalitiesDTO();
        dto.setId(modalida.getId());
        dto.setUniversityID(modalida.getUniversityID());
        dto.setModalityName(modalida.getModalityName());
        return dto;
    }

    public ModalitiesDTO insertarDatos(ModalitiesDTO data) {
        if (data == null){
            throw new IllegalArgumentException("Datos invalidos");
        }
        try {
            ModalitiesEntity entity = convertirAEntity(data);
            ModalitiesEntity ModalityGuardado = repo.save(entity);
            return convertirModalidadesADTO(ModalityGuardado);
        }catch (Exception e){
            log.error("Error al insertar el nuevo registro" + e.getMessage());
            throw new RuntimeException("Error al ingresar el nuevo registro");
        }
    }

    private ModalitiesEntity convertirAEntity(ModalitiesDTO data) {
        ModalitiesEntity entity = new ModalitiesEntity();
        entity.setUniversityID(data.getUniversityID());
        entity.setModalityName(data.getModalityName());
        return entity;
    }

    public ModalitiesDTO actualizarDatos(String id, ModalitiesDTO json) {
        ModalitiesEntity existente = repo.findById(id).orElseThrow(() -> new RuntimeException("Registro no encontrado"));
        existente.setUniversityID(json.getUniversityID());
        existente.setModalityName(json.getModalityName());
        ModalitiesEntity ModalityActualizada = repo.save(existente);
        return convertirModalidadesADTO(ModalityActualizada);
    }

    public boolean eliminarModality(String id) {
        try {
            ModalitiesEntity existente = repo.findById(id).orElse(null);
            if (existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro la modalidad", 1);
        }
    }
}
