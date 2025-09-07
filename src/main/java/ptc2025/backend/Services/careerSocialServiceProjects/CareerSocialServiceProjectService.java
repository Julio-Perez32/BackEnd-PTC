package ptc2025.backend.Services.careerSocialServiceProjects;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.SocialServiceProjects.SocialServiceProjectsEntity;
import ptc2025.backend.Entities.careerSocialServiceProjects.CareerSocialServiceProjectEntity;
import ptc2025.backend.Entities.careers.CareerEntity;
import ptc2025.backend.Models.DTO.careerSocialServiceProjects.CareerSocialServiceProjectDTO;
import ptc2025.backend.Respositories.SocialService.SocialServiceRespository;
import ptc2025.backend.Respositories.careerSocialServiceProjects.CareerSocialServiceProjectRepository;
import ptc2025.backend.Respositories.careers.CareerRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CareerSocialServiceProjectService {

    @Autowired
    private CareerSocialServiceProjectRepository repo;
    @Autowired
    private CareerRepository careerRepository;

    @Autowired
    private SocialServiceRespository socialServiceRespository;

    public List<CareerSocialServiceProjectDTO> getProjects(){
        return repo.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    public Page<CareerSocialServiceProjectDTO> getProjectPagination(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<CareerSocialServiceProjectEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertToDTO);
    }

    public CareerSocialServiceProjectDTO insertProject(CareerSocialServiceProjectDTO dto){
        if(dto == null) throw new IllegalArgumentException("Proyecto no puede ser nulo");
        if(repo.existsById(dto.getId())) throw new IllegalArgumentException("Ya existe proyecto con ese ID");
        try{
            CareerSocialServiceProjectEntity entity = convertToEntity(dto);
            CareerSocialServiceProjectEntity save = repo.save(entity);
            return convertToDTO(save);
        }catch (Exception e){
            log.error("Error al resgistrar los proyectos de servicio social disponibles para la carrera " + e.getMessage());
            throw new RuntimeException("Error interno al guardar");
        }

    }

    public CareerSocialServiceProjectDTO updateProject(String id, CareerSocialServiceProjectDTO dto){
        CareerSocialServiceProjectEntity exist = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("El dato no pudo ser actualizado. Relacion entre carrera y servicio social no encontrada"));

        if (dto.getCareerId() != null){
            CareerEntity career = careerRepository.findById(dto.getCareerId())
                    .orElseThrow(() -> new IllegalArgumentException("Carrera no encontrada con ID: " + dto.getCareerId()));
            exist.setCareer(career);
        } else {
            exist.setCareer(null);
        }

        if (dto.getSocialServiceProjectId() != null){
            SocialServiceProjectsEntity social = socialServiceRespository.findById(dto.getSocialServiceProjectId())
                    .orElseThrow(() -> new IllegalArgumentException("Servicio social no encontrado con ID: " + dto.getSocialServiceProjectId()));
            exist.setSocialServiceProject(social);
        } else {
            exist.setSocialServiceProject(null);
        }

        CareerSocialServiceProjectEntity entity;
        try {
            entity = repo.getById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error: no se encontró el proyecto con id " + id);
        }

        return convertToDTO(repo.save(entity));
    }


    public boolean deleteProject(String id){
        try{
            CareerSocialServiceProjectEntity obj = repo.findById(id).orElse(null);
            if(obj != null){
                repo.deleteById(id);
                return true;
            }
            return false;
        } catch(EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("El proyecto con id "+ id + " no existe", 1);
        }
    }

    private CareerSocialServiceProjectDTO convertToDTO(CareerSocialServiceProjectEntity entity){
        CareerSocialServiceProjectDTO dto = new CareerSocialServiceProjectDTO();
        dto.setId(entity.getId());
        if(entity.getCareer() != null){
            dto.setCareerName(entity.getCareer().getNameCareer());
            dto.setCareerId(entity.getCareer().getId());
        }else{
            dto.setCareerName("Sin Carrera Asignada");
            dto.setCareerId(null);
        }
        if(entity.getSocialServiceProject() != null){
            dto.setSocialServiceProjectName(entity.getSocialServiceProject().getSocialServiceProjectName());
            dto.setSocialServiceProjectId(entity.getSocialServiceProject().getSocialServiceProjectID());
        }else{
            dto.setSocialServiceProjectName("Sin Servicio Social Asiganado");
            dto.setSocialServiceProjectId(null);
        }

        return dto;
    }

    private CareerSocialServiceProjectEntity convertToEntity(CareerSocialServiceProjectDTO dto){
        CareerSocialServiceProjectEntity entity = new CareerSocialServiceProjectEntity();
        if (dto.getCareerId() != null){
            CareerEntity career = careerRepository.findById(dto.getCareerId())
                    .orElseThrow(() -> new IllegalArgumentException("Carrera no encontrada con ID: " + dto.getCareerId()));
            entity.setCareer(career);
        }
        if (dto.getSocialServiceProjectId() != null){
            SocialServiceProjectsEntity social = socialServiceRespository.findById(dto.getSocialServiceProjectId())
                    .orElseThrow(() -> new IllegalArgumentException("Carrera no encontrada con ID: " + dto.getSocialServiceProjectId()));
            entity.setSocialServiceProject(social);
        }


        return entity;
    }
}
