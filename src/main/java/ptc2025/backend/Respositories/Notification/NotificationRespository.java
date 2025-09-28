package ptc2025.backend.Respositories.Notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.Notification.NotificationEntity;

@Repository
public interface NotificationRespository extends JpaRepository <NotificationEntity, String> {
    Page<NotificationEntity> findAll(Pageable pageable);
}
