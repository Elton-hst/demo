package io.github.eltonhst.domain.repository;

import io.github.eltonhst.domain.entity.OwnerEntity;
import io.vavr.control.Either;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public interface OwnerRepository {

    Either<RuntimeException, UUID> createOwner(OwnerEntity ownerEntity);

    Either<RuntimeException, OwnerEntity> findByUserId(UUID userId);

}
