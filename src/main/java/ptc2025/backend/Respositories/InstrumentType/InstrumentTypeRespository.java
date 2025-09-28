package ptc2025.backend.Respositories.InstrumentType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.InstrumentType.InstrumentTypeEntity;

@Repository
public interface InstrumentTypeRespository extends JpaRepository <InstrumentTypeEntity, String> {
    Page<InstrumentTypeEntity> findAll(Pageable pageable);
}
