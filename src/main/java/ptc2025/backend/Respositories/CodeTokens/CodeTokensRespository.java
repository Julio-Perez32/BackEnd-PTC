package ptc2025.backend.Respositories.CodeTokens;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.CodeTokens.CodeTokensEntity;

@Repository
public interface CodeTokensRespository extends JpaRepository<CodeTokensEntity, String> {
    Page<CodeTokensEntity> findAll(Pageable pageable);
}
