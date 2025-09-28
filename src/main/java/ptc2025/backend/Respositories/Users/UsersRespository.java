package ptc2025.backend.Respositories.Users;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ptc2025.backend.Controller.Users.UsersController;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.Users.UsersEntity;
import ptc2025.backend.Models.DTO.Users.UsersDTO;

import java.util.Optional;

public interface UsersRespository extends JpaRepository<UsersEntity, String> {
    Optional<UsersEntity> findByEmail(String email);
    Page<UsersEntity> findAll(Pageable pageable);
}
