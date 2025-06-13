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
            log.error("[Repository] Falha ao tentar adicionar um client {}", e.getMessage());
            return Either.left(e);
        }
        final var result = dao.save(clientEntity).getUserId();
        log.info("[Repository] Sucesso ao adicionar um novo client");
        return Either.right(result);
    }

    @Override
    public Either<RuntimeException, ClientEntity> findByUserId(UUID userId) {
        final var result = dao.findByUserId(userId);
        if(result.isEmpty()) {
            log.error("[Repository] Usuário não encontrado {}", userId);
            return Either.left(new NotFoundException("Usuário não encontrado"));
        }
        log.info("[Repository] Busca do usuário finalizada");
        return Either.right(result.get());
    }

    @Override
    public Page<ClientEntity> findAll(Pageable pageable) {
        log.info("[Repository] listando todos os clients");
        return dao.findAll(pageable);
    }

    @Override
    public Long countClient() {
        log.info("[Repository] consulta número de clients");
        return dao.count();
    }
}
