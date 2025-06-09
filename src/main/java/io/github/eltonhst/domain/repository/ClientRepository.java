package io.github.eltonhst.domain.repository;

import io.github.eltonhst.domain.entity.ClientEntity;
import io.vavr.control.Either;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public interface ClientRepository {

    Either<RuntimeException, UUID> createClient(ClientEntity clientEntity);

    Either<RuntimeException, ClientEntity> findClientById(UUID userId);

    Page<ClientEntity> findAll(Pageable pageable);

    Long countClient();

}
