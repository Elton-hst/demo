package io.github.eltonhst.domain.useCase.client;

import io.github.eltonhst.domain.entity.ClientEntity;
import io.github.eltonhst.domain.exception.NotFoundException;
import io.github.eltonhst.domain.repository.ClientRepository;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class ClientSearchUseCase {

    private final ClientRepository repository;

    public ClientSearchUseCase(ClientRepository repository) {
        this.repository = repository;
    }

    public Either<RuntimeException, ClientEntity> execute(UUID userId) {
        log.info("[Service: ClientSearchUseCase] Iniciando a busca do client {}", userId);
        return repository.findClientById(userId);
    }

    public void logActiveClients() {
        log.info("Inicio da rotina");

        log.info("Quantidade de clients: {}", repository.countClient());

        log.info("Fim da rotina");
    }
}
