package io.github.eltonhst.data.client;

import io.github.eltonhst.domain.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepositoryDAO extends JpaRepository<ClientEntity, UUID> {
    Optional<ClientEntity> findByUserId(UUID uuid);
}
