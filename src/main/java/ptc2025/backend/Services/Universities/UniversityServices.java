package ptc2025.backend.Services.Universities;

import com.cloudinary.Cloudinary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ptc2025.backend.Entities.SystemPermissions.SystemPermissionsEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Exceptions.ExceptionNotFound;
import ptc2025.backend.Exceptions.ExceptionServerError;
import ptc2025.backend.Models.DTO.SystemPermissions.SystemPermissionsDTO;
import ptc2025.backend.Models.DTO.Universities.UniversityDTO;
import ptc2025.backend.Respositories.Universities.UniversityRespository;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j /** Es una anotacion de loombook que sirve para mostrar mensajes en consola
 tipo ustedes le ponen log.error("lol, algo salio mal") o log.info("Todo correcto")
 y eso les va a ayudar a saber que esta pasando lo que pasa mientras corre el programa
 */
@Service
public class UniversityServices {

    @Autowired
    UniversityRespository repo;

    public Cloudinary cloudinary;

    // Obtener todas las universidades
    public List<UniversityDTO> getUniversityService() {
        try {
            List<UniversityEntity> universidad = repo.findAll();
            return universidad.stream()
                    .map(this::convertirAUniversityDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al obtener las universidades", e);
            throw new ExceptionServerError("Error interno al obtener las universidades");
        }
    }
    public Page<UniversityDTO> getUniversityPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UniversityEntity> pageEntity = repo.findAll(pageable);

        if (pageEntity.isEmpty()) {
            throw new ExceptionNotFound("No se encontraron definiciones de materias para la página solicitada.");
        }

        return pageEntity.map(this::convertirAUniversityDTO);
    }

    // Insertar nueva universidad
    public UniversityDTO insertarUniversidad(UniversityDTO dto, MultipartFile file) {
        // Validaciones combinadas
        if (dto.getUniversityName() == null || dto.getUniversityName().isBlank() ||
                dto.getRector() == null || dto.getRector().isBlank() ||
                dto.getWebPage() == null || dto.getWebPage().isBlank()) {
            throw new IllegalArgumentException("Todos los campos obligatorios deben estar completos: nombre de universidad, rector y página web.");
        }

        // Validación de URL
        if (!dto.getWebPage().matches("^https?://.+")) {
            throw new IllegalArgumentException("La página web debe iniciar con http:// o https://");
        }

        try{
            String imageUrl = null;
            if(file != null && !file.isEmpty()){
                Map uploadResult = cloudinary.uploader().upload(file.getBytes(), Map.of());
                imageUrl = (String) uploadResult.get("secure_url");
                dto.setImageUrlUniversities(imageUrl);
            }
        }catch (IOException e){
            throw new IllegalArgumentException(e.getMessage());
        }

        try {
            UniversityEntity entidad = convertirAUniversityEntity(dto);
            UniversityEntity guardado = repo.save(entidad);
            return convertirAUniversityDTO(guardado);
        } catch (Exception e) {
            log.error("Error al registrar una nueva universidad: " + e.getMessage(), e);
            throw new ExceptionServerError("Error interno al guardar la universidad");
        }
    }

    // Actualizar universidad
    public UniversityDTO modificarUniversidad(String id, UniversityDTO dto) {
        UniversityEntity universidadExistente = repo.findById(id)
                .orElseThrow(() -> new ExceptionNotFound("El dato no pudo ser actualizado. Universidad no encontrada"));

        try {
            universidadExistente.setUniversityName(dto.getUniversityName());
            universidadExistente.setRector(dto.getRector());
            universidadExistente.setWebPage(dto.getWebPage());
            universidadExistente.setImageUrlUniversities(dto.getImageUrlUniversities());

            UniversityEntity actualizado = repo.save(universidadExistente);
            return convertirAUniversityDTO(actualizado);
        } catch (Exception e) {
            log.error("Error al actualizar la universidad con ID " + id, e);
            throw new ExceptionServerError("Error interno al actualizar la universidad");
        }
    }

    // Eliminar universidad
    public boolean elminarUniversidad(String id) {
        try {
            UniversityEntity objUniversidad = repo.findById(id).orElse(null);

            if (objUniversidad != null) {
                repo.deleteById(id);
                return true;
            } else {
                throw new ExceptionNotFound("Universidad no encontrada con el ID: " + id);
            }
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionNotFound("No se encontró ninguna universidad con el ID: " + id + " para ser eliminada");
        } catch (Exception e) {
            log.error("Error interno al eliminar la universidad con ID " + id, e);
            throw new ExceptionServerError("Error interno al eliminar la universidad");
        }
    }

    // Convertir Entity → DTO
    private UniversityDTO convertirAUniversityDTO(UniversityEntity university) {
        UniversityDTO DTO = new UniversityDTO();
        DTO.setUniversityID(university.getUniversityID());
        DTO.setUniversityName(university.getUniversityName());
        DTO.setRector(university.getRector());
        DTO.setWebPage(university.getWebPage());
        DTO.setImageUrlUniversities(university.getImageUrlUniversities());
        return DTO;
    }

    // Convertir DTO → Entity
    private UniversityEntity convertirAUniversityEntity(UniversityDTO dto) {
        UniversityEntity entity = new UniversityEntity();
        entity.setUniversityName(dto.getUniversityName());
        entity.setRector(dto.getRector());
        entity.setWebPage(dto.getWebPage());
        entity.setImageUrlUniversities(dto.getImageUrlUniversities());
        return entity;
    }
}
