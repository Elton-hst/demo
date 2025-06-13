package io.github.eltonhst.domain.useCase.owner;

import io.github.eltonhst.domain.entity.OwnerEntity;
import io.github.eltonhst.domain.repository.OwnerRepository;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class OwnerSearchUseCase {

    private final OwnerRepository repository;

    public OwnerSearchUseCase(OwnerRepository repository) {
        this.repository = repository;
    }

    public Either<RuntimeException, OwnerEntity> execute(UUID userId) {
        log.info("[Service] Iniciando a busca do client {}", userId);
        return repository.findByUserId(userId);
    }
}
