package ptc2025.backend.Respositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ptc2025.backend.Entities.UserEntity;

public interface UserRespository extends JpaRepository<UserEntity, String> {
}
