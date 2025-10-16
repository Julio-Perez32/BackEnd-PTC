package ptc2025.backend.Services.Localities;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
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
        List<LocalitiesEntity> localidad = repo.findAll();
        return localidad.stream()
                .map(this::convertirALocaltityDTO)
                .collect(Collectors.toList());
    }

    public Page<LocalitiesDTO> getLocalitiesPagination(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<LocalitiesEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirALocaltityDTO);
    }

    public LocalitiesDTO insertarLocalidad(LocalitiesDTO dto){
        if (dto.getUniversityID() == null || dto.getUniversityID().isBlank() ||
                dto.getIsMainLocality() == null ||
                dto.getAddress() == null || dto.getAddress().isBlank() ||
                dto.getPhoneNumber() == null || dto.getPhoneNumber().isBlank()) {

            throw new ExceptionBadRequest("Todos los campos obligatorios deben estar completos (universityID, isMainLocality, address, phoneNumber).");
        }
        try{
            LocalitiesEntity entidad = convertirALocaltityEntity(dto);
            LocalitiesEntity guardar = repo.save(entidad);
            return convertirALocaltityDTO(guardar);
        }catch (Exception e){
            log.error("Error al registrar una nueva localidad" +  e.getMessage());
            throw new RuntimeException("Error al intentar guardar la localidad");
        }
    }

    public LocalitiesDTO modificarLocalidad(String id, LocalitiesDTO dto){
        LocalitiesEntity localidadExistente = repo.findById(id)
                .orElseThrow(() -> new ExceptionNoSuchElement("El dato no pudo ser actualizado. Localidad no encontrada con ID: " + id));

        localidadExistente.setIsMainLocality(dto.getIsMainLocality());
        localidadExistente.setAddress(dto.getAddress());
        localidadExistente.setPhoneNumber(dto.getPhoneNumber());


        if(dto.getUniversityID() != null){
            UniversityEntity university = repoUniversity.findById(dto.getUniversityID())
                    .orElseThrow(() -> new ExceptionNoSuchElement("Universidad no encontrada con ID: " + dto.getUniversityID()));
            localidadExistente.setUniversity(university);
        } else {
            localidadExistente.setUniversity(null);
        }

        LocalitiesEntity actualizado = repo.save(localidadExistente);
        return convertirALocaltityDTO(actualizado);
    }

    public boolean eliminarLocalidad (String id){
        try {
            LocalitiesEntity objLocalidad = repo.findById(id).orElse(null);
            if (objLocalidad != null){
                repo.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("No se encontro ninguna localidad con el ID: " + id , 1);
        }
    }

    private LocalitiesDTO convertirALocaltityDTO(LocalitiesEntity localities) {
        LocalitiesDTO dto = new LocalitiesDTO();
        dto.setLocalityID(localities.getLocalityID());

        dto.setIsMainLocality(localities.getIsMainLocality());
        dto.setAddress(localities.getAddress());
        dto.setPhoneNumber(localities.getPhoneNumber());

        if(localities.getUniversity() != null){
            dto.setUniversityName(localities.getUniversity().getUniversityName());
            dto.setUniversityID(localities.getUniversity().getUniversityID());
        } else {
            dto.setUniversityName("Sin Universidad Asignada");
            dto.setUniversityID(null);
        }
        return dto;
    }

    private LocalitiesEntity convertirALocaltityEntity(LocalitiesDTO dto){
        LocalitiesEntity entity = new LocalitiesEntity();

        entity.setIsMainLocality(dto.getIsMainLocality());
        entity.setAddress(dto.getAddress());
        entity.setPhoneNumber(dto.getPhoneNumber());

        if(dto.getUniversityID() != null){
            UniversityEntity university = repoUniversity.findById(dto.getUniversityID())
                    .orElseThrow(() -> new ExceptionNoSuchElement("Universidad no encontrada con ID: " + dto.getUniversityID()));
            entity.setUniversity(university);
        }

        return entity;
    }
}
