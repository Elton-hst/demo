package io.github.eltonhst.data.owner;

import io.github.eltonhst.domain.entity.OwnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OwnerRepositoryDAO extends JpaRepository<OwnerEntity, UUID> {
    Optional<OwnerEntity> findByUserId(UUID userId);
}
