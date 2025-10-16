package ptc2025.backend.Services.Localities;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.Localities.LocalitiesEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Exceptions.ExceptionBadRequest;
import ptc2025.backend.Exceptions.ExceptionNoSuchElement;
import ptc2025.backend.Models.DTO.Localities.LocalitiesDTO;
import ptc2025.backend.Respositories.Localities.LocalitiesRespository;
import ptc2025.backend.Respositories.Universities.UniversityRespository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LocalitiesService {
    @Autowired
    LocalitiesRespository repo;

    @Autowired
    UniversityRespository repoUniversity;

    public List<LocalitiesDTO> getLocalitiesService(){
        try {
            log.debug("Obteniendo todas las localidades");
            List<LocalitiesEntity> localidad = repo.findAll();
            log.debug("Se encontraron {} localidades", localidad.size());
            return localidad.stream()
                    .map(this::convertirALocaltityDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al obtener localidades", e);
            throw new RuntimeException("Error al obtener localidades: " + e.getMessage(), e);
        }
    }

    public Page<LocalitiesDTO> getLocalitiesPagination(int page, int size){
        try {
            log.debug("Obteniendo localidades paginadas - página: {}, tamaño: {}", page, size);
            Pageable pageable = PageRequest.of(page, size);
            Page<LocalitiesEntity> pageEntity = repo.findAll(pageable);
            log.debug("Página obtenida con {} elementos", pageEntity.getNumberOfElements());
            return pageEntity.map(this::convertirALocaltityDTO);
        } catch (Exception e) {
            log.error("Error al obtener localidades paginadas", e);
            throw new RuntimeException("Error al obtener localidades paginadas: " + e.getMessage(), e);
        }
    }

    public LocalitiesDTO insertarLocalidad(LocalitiesDTO dto){
        // VALIDACIÓN CORREGIDA - isMainLocality puede ser false sin problema
        if (dto.getUniversityID() == null || dto.getUniversityID().isBlank()) {
            throw new ExceptionBadRequest("El ID de universidad es requerido");
        }

        if (dto.getAddress() == null || dto.getAddress().isBlank()) {
            throw new ExceptionBadRequest("La dirección es requerida");
        }

        if (dto.getPhoneNumber() == null || dto.getPhoneNumber().isBlank()) {
            throw new ExceptionBadRequest("El número de teléfono es requerido");
        }

        // Asegurar que isMainLocality nunca sea null - usar false por defecto
        if (dto.getIsMainLocality() == null) {
            dto.setIsMainLocality(false);
        }

        try{
            LocalitiesEntity entidad = convertirALocaltityEntity(dto);
            LocalitiesEntity guardar = repo.save(entidad);
            log.info("Localidad creada exitosamente: {}", guardar.getLocalityID());
            return convertirALocaltityDTO(guardar);
        }catch (Exception e){
            log.error("Error al registrar una nueva localidad: {}", e.getMessage(), e);
            throw new RuntimeException("Error al intentar guardar la localidad: " + e.getMessage());
        }
    }

    public LocalitiesDTO modificarLocalidad(String id, LocalitiesDTO dto){
        log.info("Modificando localidad con ID: {}", id);

        LocalitiesEntity localidadExistente = repo.findById(id)
                .orElseThrow(() -> {
                    log.warn("Localidad no encontrada con ID: {}", id);
                    return new ExceptionNoSuchElement("El dato no pudo ser actualizado. Localidad no encontrada con ID: " + id);
                });

        // Actualizar campos - asegurar que isMainLocality nunca sea null
        localidadExistente.setIsMainLocality(dto.getIsMainLocality() != null ? dto.getIsMainLocality() : false);
        localidadExistente.setAddress(dto.getAddress());
        localidadExistente.setPhoneNumber(dto.getPhoneNumber());

        if(dto.getUniversityID() == null || dto.getUniversityID().isBlank()){
            log.warn("Intento de actualizar localidad sin universityID");
            throw new ExceptionBadRequest("El ID de universidad es requerido");
        }

        UniversityEntity university = repoUniversity.findById(dto.getUniversityID())
                .orElseThrow(() -> {
                    log.warn("Universidad no encontrada con ID: {}", dto.getUniversityID());
                    return new ExceptionNoSuchElement("Universidad no encontrada con ID: " + dto.getUniversityID());
                });
        localidadExistente.setUniversity(university);

        LocalitiesEntity actualizado = repo.save(localidadExistente);
        log.info("Localidad actualizada exitosamente: {}", id);
        return convertirALocaltityDTO(actualizado);
    }

    public boolean eliminarLocalidad(String id){
        try {
            LocalitiesEntity objLocalidad = repo.findById(id).orElse(null);
            if (objLocalidad != null){
                repo.deleteById(id);
                log.info("Localidad eliminada: {}", id);
                return true;
            } else {
                log.warn("Localidad no encontrada para eliminar: {}", id);
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            log.error("Error al eliminar localidad: {}", id, e);
            throw new EmptyResultDataAccessException("No se encontro ninguna localidad con el ID: " + id, 1);
        }
    }

    private LocalitiesDTO convertirALocaltityDTO(LocalitiesEntity localities) {
        try {
            LocalitiesDTO dto = new LocalitiesDTO();
            dto.setLocalityID(localities.getLocalityID());

            // Asegurar que isMainLocality nunca sea null en el DTO
            dto.setIsMainLocality(localities.getIsMainLocality() != null ? localities.getIsMainLocality() : false);

            dto.setAddress(localities.getAddress());
            dto.setPhoneNumber(localities.getPhoneNumber());

            // Manejo seguro de la relación con University
            if(localities.getUniversity() != null){
                dto.setUniversityName(localities.getUniversity().getUniversityName());
                dto.setUniversityID(localities.getUniversity().getUniversityID());
            } else {
                dto.setUniversityName("Sin Universidad Asignada");
                dto.setUniversityID(null);
            }
            return dto;
        } catch (Exception e) {
            log.error("Error al convertir LocalitiesEntity a DTO", e);
            throw new RuntimeException("Error en conversión a DTO: " + e.getMessage(), e);
        }
    }

    private LocalitiesEntity convertirALocaltityEntity(LocalitiesDTO dto){
        try {
            LocalitiesEntity entity = new LocalitiesEntity();

            // Asegurar que isMainLocality nunca sea null
            entity.setIsMainLocality(dto.getIsMainLocality() != null ? dto.getIsMainLocality() : false);

            entity.setAddress(dto.getAddress());
            entity.setPhoneNumber(dto.getPhoneNumber());

            if(dto.getUniversityID() != null && !dto.getUniversityID().isBlank()){
                UniversityEntity university = repoUniversity.findById(dto.getUniversityID())
                        .orElseThrow(() -> new ExceptionNoSuchElement("Universidad no encontrada con ID: " + dto.getUniversityID()));
                entity.setUniversity(university);
            } else {
                throw new ExceptionBadRequest("El ID de universidad es requerido");
            }
            return entity;
        } catch (Exception e) {
            log.error("Error al convertir DTO a LocalitiesEntity", e);
            throw new RuntimeException("Error en conversión a Entity: " + e.getMessage(), e);
        }
    }
}