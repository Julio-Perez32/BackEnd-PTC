package ptc2025.backend.Respositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ptc2025.backend.Entities.UsersEntity;

public interface UsersRespository extends JpaRepository<UsersEntity, String> {
}
