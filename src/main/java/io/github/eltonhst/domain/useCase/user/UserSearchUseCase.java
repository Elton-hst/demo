package io.github.eltonhst.domain.useCase.user;

import io.github.eltonhst.domain.entity.UserEntity;
import io.github.eltonhst.domain.exception.BadRequestException;
import io.github.eltonhst.domain.exception.NotFoundException;
import io.github.eltonhst.infra.mapper.MapperUser;
import io.github.eltonhst.infra.properties.KeyclockProperties;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.UUID;

import static io.github.eltonhst.infra.mapper.MapperUser.toEntity;

@Slf4j
@Service
public class UserSearchUseCase {

    private final Keycloak keycloak;
    private final KeyclockProperties properties;
    private final KeyclockProperties keyclockProperties;

    public UserSearchUseCase(Keycloak keycloak, KeyclockProperties properties, KeyclockProperties keyclockProperties) {
        this.keycloak = keycloak;
        this.properties = properties;
        this.keyclockProperties = keyclockProperties;
    }

    public Either<RuntimeException, UserEntity> findByUsername(String username) {
        log.info("[Service] Iniciando a busca por username {}", username);
        final var user = keycloak
                .realm(keyclockProperties.getRealm())
                .users()
                .searchByUsername(username, true)
                .stream()
                .findFirst();

        if(user.isEmpty()) {
            log.error("[Service] Usuário não encontrado");
            return Either.left(new NotFoundException("Usuário não encontrado"));
        }
        log.info("[Service] Sucesso ao buscar o usuário");
        return Either.right(toEntity(user.get()));
    }

    public Either<RuntimeException, UserEntity> findByEmail(String email) {
        log.info("[Service] Iniciando a busca por username {}", email);
        final var user = keycloak
                .realm(keyclockProperties.getRealm())
                .users()
                .searchByEmail(email, true)
                .stream()
                .findFirst();

        if(user.isEmpty()) {
            log.error("[Service] Usuário não encontrado");
            return Either.left(new NotFoundException("Usuário não encontrado"));
        }
        log.info("[Service] Sucesso ao buscar o usuário");
        return Either.right(toEntity(user.get()));
    }

    public Either<RuntimeException, UserEntity> findById(Principal principal) {
        log.info("[Service] Iniciando a busca por id {}", principal.getName());
        try {
            final var userRepresentation = keycloak
                    .realm(properties.getRealm())
                    .users()
                    .get(principal.getName())
                    .toRepresentation();

            if(userRepresentation == null) {
                log.error("[Service] Usuário não encontrado");
                return Either.left(new NotFoundException("Usuário não encontrado"));
            }

            log.info("[Service] Sucesso ao buscar o usuário");
            return Either.right(MapperUser.toEntity(userRepresentation));
        } catch (RuntimeException e) {
            log.error("[Service] Falha ao tentar buscar o usuário {}", e.getMessage());
            return Either.left(new BadRequestException("Falha ao tentar buscar o usuário"));
        }
    }

    public Either<RuntimeException, UserEntity> findById(UUID userId) {
        log.info("[Service] Iniciando a busca por id {}", userId);
        try {
            final var userRepresentation = keycloak
                    .realm(properties.getRealm())
                    .users()
                    .get(userId.toString())
                    .toRepresentation();

            if(userRepresentation == null) {
                log.error("[Service] Usuário não encontrado");
                return Either.left(new NotFoundException("Usuário não encontrado"));
            }

            log.info("[Service] Sucesso ao buscar o usuário");
            return Either.right(MapperUser.toEntity(userRepresentation));
        } catch (RuntimeException e) {
            log.error("[Service] Falha ao tentar buscar o usuário {}", e.getMessage());
            return Either.left(new BadRequestException("Falha ao tentar buscar o usuário"));
        }
    }
}
