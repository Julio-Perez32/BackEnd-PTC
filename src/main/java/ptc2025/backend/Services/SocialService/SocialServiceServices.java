package ptc2025.backend.Services.SocialService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.SocialServiceProjects.SocialServiceProjectsEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Models.DTO.SocialService.SocialServiceDTO;
import ptc2025.backend.Respositories.SocialService.SocialServiceRespository;
import ptc2025.backend.Respositories.Universities.UniversityRespository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SocialServiceServices {
    @Autowired
    SocialServiceRespository repo;

    @Autowired //Se inyecta el repositorio de University
    UniversityRespository repoUniversity;

    public List<SocialServiceDTO> getSocialService() {
        List<SocialServiceProjectsEntity> servicioSocial = repo.findAll();
        if (servicioSocial.isEmpty()) {
            throw new RuntimeException("No existen registros de proyectos de servicio social");
        }
        return servicioSocial.stream()
                .map(this::convertirSSDTO)
                .collect(Collectors.toList());
    }

    public Page<SocialServiceDTO> getSocialServicePagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SocialServiceProjectsEntity> pageEntity = repo.findAll(pageable);
        if (pageEntity.isEmpty()) {
            throw new RuntimeException("No existen registros para mostrar en la paginación");
        }
        return pageEntity.map(this::convertirSSDTO);
    }

    public SocialServiceDTO insertarServicioSocial(SocialServiceDTO dto) {
        if (dto.getUniversityID() == null || dto.getUniversityID().isBlank() ||
                dto.getSocialServiceProjectName() == null || dto.getSocialServiceProjectName().isBlank() ||
                dto.getDescription() == null || dto.getDescription().isBlank()) {
            throw new IllegalArgumentException("Todos los campos deben de estar completos");
        }
        try {
            SocialServiceProjectsEntity entidad = convertirSSEntity(dto);
            SocialServiceProjectsEntity guardar = repo.save(entidad);
            return convertirSSDTO(guardar);
        } catch (Exception e) {
            log.error("Error al registrar el servicio social del estudiante: " + e.getMessage());
            throw new RuntimeException("Error al intentar guardar el servicio social");
        }
    }

    public SocialServiceDTO modificarServicioSocial(String id, SocialServiceDTO dto) {
        SocialServiceProjectsEntity servicioExistente = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("El dato no pudo ser actualizado. Localidad no encontrada"));

        servicioExistente.setSocialServiceProjectName(dto.getSocialServiceProjectName());
        servicioExistente.setDescription(dto.getDescription());

        if (dto.getUniversityID() != null) {
            UniversityEntity university = repoUniversity.findById(dto.getUniversityID())
                    .orElseThrow(() -> new IllegalArgumentException("Universidad no encontrada con ID: " + dto.getUniversityID()));
            servicioExistente.setUniversity(university);
        } else {
            servicioExistente.setUniversity(null);
        }

        try {
            SocialServiceProjectsEntity actualizado = repo.save(servicioExistente);
            return convertirSSDTO(actualizado);
        } catch (Exception e) {
            log.error("Error al actualizar el servicio social: " + e.getMessage());
            throw new RuntimeException("Error inesperado al actualizar el servicio social");
        }
    }

    public boolean eliminarServicioSocial(String id) {
        try {
            SocialServiceProjectsEntity objServicio = repo.findById(id).orElse(null);
            if (objServicio != null) {
                repo.deleteById(id);
                return true;
            } else {
                System.out.println("Servicio social no encontrado");
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("No se encontro ninguna servicio social con el ID " + id, 1);
        } catch (Exception e) {
            log.error("Error inesperado al eliminar el servicio social: " + e.getMessage());
            throw new RuntimeException("Error inesperado al eliminar el servicio social");
        }
    }

    private SocialServiceDTO convertirSSDTO(SocialServiceProjectsEntity entity) {
        SocialServiceDTO dto = new SocialServiceDTO();
        dto.setSocialServiceProjectID(entity.getSocialServiceProjectID());
        dto.setSocialServiceProjectName(entity.getSocialServiceProjectName());
        dto.setDescription(entity.getDescription());

        if (entity.getUniversity() != null) {
            dto.setUniversityName(entity.getUniversity().getUniversityName());
            dto.setUniversityID(entity.getUniversity().getUniversityID());
        } else {
            dto.setUniversityName("Sin Universidad Asignada");
            dto.setUniversityID(null);
        }
        return dto;
    }

    private SocialServiceProjectsEntity convertirSSEntity(SocialServiceDTO dto) {
        SocialServiceProjectsEntity entity = new SocialServiceProjectsEntity();
        entity.setSocialServiceProjectName(dto.getSocialServiceProjectName());
        entity.setDescription(dto.getDescription());

        if (dto.getUniversityID() != null) {
            UniversityEntity university = repoUniversity.findById(dto.getUniversityID())
                    .orElseThrow(() -> new IllegalArgumentException("Universidad no encontrada con ID: " + dto.getUniversityID()));
            entity.setUniversity(university);
        }

        return entity;
    }
}
