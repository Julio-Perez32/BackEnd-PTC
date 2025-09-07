package ptc2025.backend.Services.Modalities;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.Modalities.ModalitiesEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Exceptions.ExceptionBadRequest;
import ptc2025.backend.Exceptions.ExceptionNoSuchElement;
import ptc2025.backend.Models.DTO.Modalities.ModalitiesDTO;
import ptc2025.backend.Respositories.Modalities.ModalityRepository;
import ptc2025.backend.Respositories.Universities.UniversityRespository;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class ModalityService {

    @Autowired
    private ModalityRepository repo;

    @Autowired
    private UniversityRespository repoUniversity;

    public List<ModalitiesDTO> getAllModalities(){
        List<ModalitiesEntity> modalities = repo.findAll();
        return modalities.stream()
                .map(this::convertirModalidadesADTO)
                .collect(Collectors.toList());
    }

    public Page<ModalitiesDTO> getModalitiesPagination(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<ModalitiesEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirModalidadesADTO);
    }

    public ModalitiesDTO convertirModalidadesADTO(ModalitiesEntity modalida){
        ModalitiesDTO dto = new ModalitiesDTO();
        dto.setId(modalida.getId());
        dto.setModalityName(modalida.getModalityName());

        if(modalida.getUniversity() != null){
            dto.setUniversityName(modalida.getUniversity().getUniversityName());
            dto.setUniversityID(modalida.getUniversity().getUniversityID());
        } else {
            dto.setUniversityName("Sin Universidad Asignada");
            dto.setUniversityID(null);
        }
        return dto;
    }

    public ModalitiesDTO insertarDatos(ModalitiesDTO data) {
        if (data == null) {
            throw new ExceptionBadRequest("Datos inválidos: el objeto no puede ser nulo.");
        }
        try {
            ModalitiesEntity entity = convertirAEntity(data);
            ModalitiesEntity ModalityGuardado = repo.save(entity);
            return convertirModalidadesADTO(ModalityGuardado);
        } catch (Exception e) {
            log.error("Error al insertar el nuevo registro: " + e.getMessage());
            throw new RuntimeException("Error al ingresar el nuevo registro");
        }
    }

    private ModalitiesEntity convertirAEntity(ModalitiesDTO data) {
        ModalitiesEntity entity = new ModalitiesEntity();
        entity.setModalityName(data.getModalityName());
        if (data.getUniversityID() != null) {
            UniversityEntity university = repoUniversity.findById(data.getUniversityID())
                    .orElseThrow(() -> new ExceptionNoSuchElement("Universidad no encontrada con ID: " + data.getUniversityID()));
            entity.setUniversity(university);
        }
        return entity;
    }

    public ModalitiesDTO actualizarDatos(String id, ModalitiesDTO json) {
        ModalitiesEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionNoSuchElement("Registro no encontrado con ID: " + id));

        existente.setModalityName(json.getModalityName());

        if (json.getUniversityID() != null) {
            UniversityEntity university = repoUniversity.findById(json.getUniversityID())
                    .orElseThrow(() -> new ExceptionNoSuchElement("Universidad no encontrada con ID: " + json.getUniversityID()));
            existente.setUniversity(university);
        } else {
            existente.setUniversity(null);
        }

        ModalitiesEntity ModalityActualizada = repo.save(existente);
        return convertirModalidadesADTO(ModalityActualizada);
    }

    public boolean eliminarModality(String id) {
        try {
            ModalitiesEntity existente = repo.findById(id).orElse(null);
            if (existente != null) {
                repo.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("No se encontró la modalidad con el ID: " + id, 1);
        }
    }
}
