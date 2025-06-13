package io.github.eltonhst.domain.useCase.client;

import io.github.eltonhst.domain.entity.ClientEntity;
import io.github.eltonhst.domain.entity.UserEntity;
import io.github.eltonhst.domain.enums.AuthEnum;
import io.github.eltonhst.domain.exception.BadRequestException;
import io.github.eltonhst.domain.repository.ClientRepository;
import io.github.eltonhst.domain.useCase.auth.AuthAddRoleUseCase;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
public class ClientCreateUseCase {

    private final ClientRepository repository;
    private final AuthAddRoleUseCase addRoleToUser;

    public ClientCreateUseCase(ClientRepository repository, AuthAddRoleUseCase addRoleToUser) {
        this.repository = repository;
        this.addRoleToUser = addRoleToUser;
    }

    @Transactional
    public Either<RuntimeException, UUID> execute(UserEntity user) {
        try {
            log.info("[Service] Iniciando a criação do client {}", user.getUserId());
            addRoleToUser.execute(
                    user.getUserId(),
                    AuthEnum.CLIENT.getRole()
            );
            final ClientEntity client = getClientEntity(user);
            return repository.createClient(client);
        } catch (RuntimeException e) {
            log.error("[Service] Erro ao tentar criar um novo client");
            return Either.left(new BadRequestException(e.getMessage()));
        }
    }

    private static ClientEntity getClientEntity(UserEntity user) {
        return ClientEntity.builder()
                .id(UUID.randomUUID())
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

}
