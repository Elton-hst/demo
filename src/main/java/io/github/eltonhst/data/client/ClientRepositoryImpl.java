package io.github.eltonhst.data.client;

import io.github.eltonhst.domain.entity.ClientEntity;
import io.github.eltonhst.domain.exception.NotFoundException;
import io.github.eltonhst.domain.repository.ClientRepository;
import io.github.eltonhst.domain.validation.ValidationClientImpl;
import io.github.eltonhst.domain.validation.Validator;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Slf4j
@Repository
public class ClientRepositoryImpl implements ClientRepository {

    private final ClientRepositoryDAO dao;

    public ClientRepositoryImpl(ClientRepositoryDAO dao) {
        this.dao = dao;
    }

    @Override
    public Either<RuntimeException, UUID> createClient(ClientEntity clientEntity) {
        try {
            Validator.validate(new ValidationClientImpl(), clientEntity);
        } catch(RuntimeException e) {
            return Either.left(e);
        }
        final var result = dao.save(clientEntity).getId();
        return Either.right(result);
    }

    @Override
    public Either<RuntimeException, ClientEntity> findClientById(UUID userId) {
        final var result = dao.findByUserId(userId);
        if(result.isEmpty()) {
            log.error("[Repository: ClientRepositoryImpl] Usuário não encontrado {}", userId);
            Either.left(new NotFoundException("Usuário não encontrado"));
        }
        log.info("[Repository: ClientRepositoryImpl] Busca do usuário finalizada");
        return Either.right(result.get());
    }

    @Override
    public Page<ClientEntity> findAll(Pageable pageable) {
        return dao.findAll(pageable);
    }

    @Override
    public Long countClient() {
        return dao.count();
    }
}
