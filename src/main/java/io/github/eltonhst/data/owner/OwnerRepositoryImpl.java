package io.github.eltonhst.data.owner;

import io.github.eltonhst.domain.entity.OwnerEntity;
import io.github.eltonhst.domain.exception.NotFoundException;
import io.github.eltonhst.domain.repository.OwnerRepository;
import io.github.eltonhst.domain.validation.ValidationOwnerImpl;
import io.github.eltonhst.domain.validation.Validator;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Slf4j
@Repository
public class OwnerRepositoryImpl implements OwnerRepository {

    private final OwnerRepositoryDAO dao;

    public OwnerRepositoryImpl(OwnerRepositoryDAO dao) {
        this.dao = dao;
    }

    @Override
    public Either<RuntimeException, UUID> createOwner(OwnerEntity ownerEntity) {
        try {
            Validator.validate(new ValidationOwnerImpl(), ownerEntity);
        } catch(RuntimeException e) {
            return Either.left(e);
        }
        final var result = dao.save(ownerEntity).getId();
        return Either.right(result);
    }

    @Override
    public Either<RuntimeException, OwnerEntity> findOwnerById(UUID userId) {
        final var result = dao.findByUserId(userId);
        if(result.isEmpty()) {
            log.error("[Repository: OwnerRepositoryImpl] Usuário não encontrado {}", userId);
            Either.left(new NotFoundException("Usuário não encontrado"));
        }
        log.info("[Repository: OwnerRepositoryImpl] Busca do usuário finalizada");
        return Either.right(result.get());
    }
}
