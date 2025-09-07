package ptc2025.backend.Services.personTypes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Entities.personTypes.personTypesEntity;
import ptc2025.backend.Exceptions.ExceptionBadRequest;
import ptc2025.backend.Exceptions.ExceptionNoSuchElement;
import ptc2025.backend.Models.DTO.Universities.UniversityDTO;
import ptc2025.backend.Models.DTO.personTypes.personTypesDTO;
import ptc2025.backend.Respositories.personTypes.personTypesRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class personTypesService {
    @Autowired
    personTypesRepository repo;

    public List<personTypesDTO> getPersonType() {
        List<personTypesEntity> universidad = repo.findAll();
        return universidad.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Page<personTypesDTO> getPersonTypePagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<personTypesEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirADTO);
    }

    public personTypesDTO insertPersonTypes(personTypesDTO dto) {
        // Validaciones combinadas
        if (dto.getPersonType() == null || dto.getPersonType().isBlank()) {
            throw new ExceptionBadRequest("Todos los campos obligatorios deben estar completos: Tipo de persona");
        }
        try {
            // Convertir DTO → Entity
            personTypesEntity entidad = convertirAEntity(dto);

            // Guardar en BD
            personTypesEntity guardado = repo.save(entidad);

            // Convertir Entity → DTO
            return convertirADTO(guardado);
        } catch (Exception e) {
            log.error("Error al registrar un nuevo tipo de persona " + e.getMessage());
            throw new RuntimeException("Error interno al guardar el tipo de persona");
        }
    }

    public personTypesDTO updatePersonTypes(String id, personTypesDTO dto) {
        personTypesEntity typersonExistente = repo.findById(id)
                .orElseThrow(() -> new ExceptionNoSuchElement("El dato no pudo ser actualizado. Tipo de persona no encontrada"));
        // Actualización de los datos
        typersonExistente.setPersonType(dto.getPersonType());

        personTypesEntity actulizado = repo.save(typersonExistente);
        return convertirADTO(actulizado);
    }

    public boolean deletePersonTypes(String id) {
        try {
            // Validación de existencia de Tipo de persona
            personTypesEntity objPerson = repo.findById(id).orElse(null);
            // Si existe se procede a eliminar
            if (objPerson != null) {
                repo.deleteById(id);
                return true;
            } else {
                System.out.println("Tipo de persona no encontrado");
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionNoSuchElement("No se encontró ningún tipo de persona con el ID: " + id + " para poder ser eliminada");
        }
    }

    private personTypesDTO convertirADTO(personTypesEntity entity) {
        personTypesDTO dto = new personTypesDTO();
        dto.setPersonTypeID(entity.getPersonTypeID());
        dto.setPersonType(entity.getPersonType());
        return dto;
    }

    private personTypesEntity convertirAEntity(personTypesDTO dto) {
        personTypesEntity entity = new personTypesEntity();
        entity.setPersonType(dto.getPersonType()); // corregido: se asigna desde el DTO
        return entity;
    }
}
