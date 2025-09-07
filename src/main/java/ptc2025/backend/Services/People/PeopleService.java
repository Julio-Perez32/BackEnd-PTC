package ptc2025.backend.Services.People;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.People.PeopleEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Entities.personTypes.personTypesEntity;
import ptc2025.backend.Exceptions.ExceptionBadRequest;
import ptc2025.backend.Exceptions.ExceptionNoSuchElement;
import ptc2025.backend.Models.DTO.People.PeopleDTO;
import ptc2025.backend.Respositories.People.PeopleRepository;
import ptc2025.backend.Respositories.personTypes.personTypesRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PeopleService {

    @Autowired
    PeopleRepository repo;

    @Autowired
    personTypesRepository repoTypesPerson;

    public List<PeopleDTO> getAllPeople() {
        List<PeopleEntity> people = repo.findAll();
        return people.stream()
                .map(this::convertToPeopleDTO)
                .collect(Collectors.toList());
    }

    public Page<PeopleDTO> getPeoplePagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PeopleEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertToPeopleDTO);
    }

    private PeopleDTO convertToPeopleDTO(PeopleEntity people) {
        PeopleDTO dto = new PeopleDTO();
        dto.setPersonID(people.getPersonID());
        dto.setFirstName(people.getFirstName());
        dto.setLastName(people.getLastName());
        dto.setBirthDate(people.getBirthDate());
        dto.setContactEmail(people.getContactEmail());
        dto.setPhone(people.getPhone());

        if (people.getPersonTypes() != null) {
            dto.setPersonType(people.getPersonTypes().getPersonType());
            dto.setPersonTypesID(people.getPersonTypes().getPersonTypeID());
        } else {
            dto.setPersonType("Sin Tipo de Persona Asignado");
            dto.setPersonTypesID(null);
        }
        return dto;
    }

    private PeopleEntity convertToPeopleEntity(PeopleDTO dto) {
        PeopleEntity entity = new PeopleEntity();
        entity.setPersonID(dto.getPersonID());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setBirthDate(dto.getBirthDate());
        entity.setContactEmail(dto.getContactEmail());
        entity.setPhone(dto.getPhone());

        if (dto.getPersonTypesID() != null) {
            personTypesEntity personTypes = repoTypesPerson.findById(dto.getPersonTypesID())
                    .orElseThrow(() -> new ExceptionNoSuchElement("Tipo de persona no encontrada con ID: " + dto.getPersonTypesID()));
            entity.setPersonTypes(personTypes);
        }
        return entity;
    }

    public PeopleDTO insertPeople(PeopleDTO dto) {
        if (dto == null || dto.getFirstName() == null || dto.getFirstName().isEmpty()
                || dto.getLastName() == null || dto.getLastName().isEmpty()
                || dto.getBirthDate() == null
                || dto.getContactEmail() == null || dto.getContactEmail().isEmpty()
                || dto.getPhone() == null || dto.getPhone().isEmpty()) {
            throw new ExceptionBadRequest("Debe llenar todos los campos de la persona.");
        }

        try {
            PeopleEntity entity = convertToPeopleEntity(dto);
            PeopleEntity peopleSaved = repo.save(entity);
            return convertToPeopleDTO(peopleSaved);
        } catch (Exception e) {
            log.error("Error al registrar a la persona: " + e.getMessage());
            throw new RuntimeException("Error al registrar el usuario");
        }
    }

    public PeopleDTO updatePeople(String id, PeopleDTO json) {
        PeopleEntity existsPeople = repo.findById(id)
                .orElseThrow(() -> new ExceptionNoSuchElement("Persona no encontrada con ID: " + id));

        existsPeople.setFirstName(json.getFirstName());
        existsPeople.setLastName(json.getLastName());
        existsPeople.setBirthDate(json.getBirthDate());
        existsPeople.setContactEmail(json.getContactEmail());
        existsPeople.setPhone(json.getPhone());

        if (json.getPersonTypesID() != null) {
            personTypesEntity personTypes = repoTypesPerson.findById(json.getPersonTypesID())
                    .orElseThrow(() -> new ExceptionNoSuchElement("Tipo de persona no encontrada con ID: " + json.getPersonTypesID()));
            existsPeople.setPersonTypes(personTypes);
        } else {
            existsPeople.setPersonTypes(null);
        }

        PeopleEntity updatedPeople = repo.save(existsPeople);
        return convertToPeopleDTO(updatedPeople);
    }

    public boolean deletePeople(String id) {
        try {
            PeopleEntity existsPeople = repo.findById(id).orElse(null);
            if (existsPeople != null) {
                repo.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionNoSuchElement("No se encontró persona con ID: " + id + " para eliminar");
        }
    }
}
