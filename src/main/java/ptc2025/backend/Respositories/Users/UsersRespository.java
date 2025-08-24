package ptc2025.backend.Respositories.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import ptc2025.backend.Entities.Users.UsersEntity;

public interface UsersRespository extends JpaRepository<UsersEntity, String> {
}
