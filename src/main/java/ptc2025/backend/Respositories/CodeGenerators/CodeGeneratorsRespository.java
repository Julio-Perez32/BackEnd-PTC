package ptc2025.backend.Respositories.CodeGenerators;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.CodeGenerators.CodeGeneratorsEntity;

public interface CodeGeneratorsRespository extends JpaRepository<CodeGeneratorsEntity, String> {
    Page<CodeGeneratorsEntity> findAll(Pageable pageable);
}
