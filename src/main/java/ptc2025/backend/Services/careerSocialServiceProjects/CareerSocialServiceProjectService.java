package ptc2025.backend.Services.careerSocialServiceProjects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.careerSocialServiceProjects.CareerSocialServiceProjectEntity;
import ptc2025.backend.Models.DTO.careerSocialServiceProjects.CareerSocialServiceProjectDTO;
import ptc2025.backend.Respositories.careerSocialServiceProjects.CareerSocialServiceProjectRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CareerSocialServiceProjectService {

    @Autowired
    private CareerSocialServiceProjectRepository repo;

    public List<CareerSocialServiceProjectDTO> getProjects(){
        return repo.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CareerSocialServiceProjectDTO insertProject(CareerSocialServiceProjectDTO dto){
        if(dto == null) throw new IllegalArgumentException("Proyecto no puede ser nulo");
        if(repo.existsById(dto.getId())) throw new IllegalArgumentException("Ya existe proyecto con ese ID");
        CareerSocialServiceProjectEntity entity = convertToEntity(dto);
        return convertToDTO(repo.save(entity));
    }

    public CareerSocialServiceProjectDTO updateProject(String id, CareerSocialServiceProjectDTO dto){
        if(!repo.existsById(id)) throw new IllegalArgumentException("No se encontró proyecto con ID: " + id);
        CareerSocialServiceProjectEntity entity = repo.getById(id);
        entity.setCareerId(dto.getCareerId());
        entity.setProjectName(dto.getProjectName());
        entity.setSupervisorName(dto.getSupervisorName());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setIsActive(dto.getIsActive());
        return convertToDTO(repo.save(entity));
    }

    public boolean deleteProject(String id){
        try{
            if(repo.existsById(id)){
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
        dto.setCareerId(entity.getCareerId());
        dto.setProjectName(entity.getProjectName());
        dto.setSupervisorName(entity.getSupervisorName());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }

    private CareerSocialServiceProjectEntity convertToEntity(CareerSocialServiceProjectDTO dto){
        CareerSocialServiceProjectEntity entity = new CareerSocialServiceProjectEntity();
        entity.setId(dto.getId());
        entity.setCareerId(dto.getCareerId());
        entity.setProjectName(dto.getProjectName());
        entity.setSupervisorName(dto.getSupervisorName());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setIsActive(dto.getIsActive());
        return entity;
    }
}
