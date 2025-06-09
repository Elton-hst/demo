package io.github.eltonhst.domain.useCase.user;

import io.github.eltonhst.domain.entity.UserEntity;
import io.github.eltonhst.domain.exception.BadRequestException;
import io.github.eltonhst.domain.exception.NotFoundException;
import io.github.eltonhst.infra.mapper.MapperUser;
import io.github.eltonhst.infra.properties.KeyclockProperties;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
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
        log.info("[Service: UserSearchUseCase] Iniciando a busca por username {}", username);
        final var user = keycloak
                .realm(keyclockProperties.getRealm())
                .users()
                .search(username)
                .stream()
                .findFirst();

        if(user.isEmpty()) {
            log.error("[Service: UserSearchUseCase] Iniciando a busca por username {}", username);
            return Either.left(new NotFoundException("Usuário não encontrado"));
        }
        log.info("[Service: UserSearchUseCase] Iniciando a busca por username {}", username);
        return Either.right(toEntity(user.get()));

        //return user.<Either<RuntimeException, UserEntity>>map(
        //        userRepresentation -> Either.right(toEntity(userRepresentation))
        //).orElseGet(() -> Either.left(new NotFoundException("Usuário não encontrado")));
    }

    public Either<RuntimeException, UserEntity> findById(Principal principal) {
        log.info("[Service: UserSearchUseCase] Iniciando a busca por id {}", principal.getName());
        try {
            final UsersResource usersResource = keycloak.realm(properties.getRealm()).users();
            final var result = usersResource.get(principal.getName());

            if(result == null) {
                log.error("[Service: UserSearchUseCase] Usuário não encontrado {}", principal.getName());
                return Either.left(new NotFoundException("Usuário não encontrado"));
            }

            log.info("[Service: UserSearchUseCase] Sucesso ao buscar o usuário");
            return Either.right(MapperUser.toEntity(result.toRepresentation()));
        } catch (RuntimeException e) {
            log.error("[Service: UserSearchUseCase] Falha ao tentar buscar o usuário {}", e.getMessage());
            return Either.left(new BadRequestException("Falha ao tentar buscar o usuário"));
        }
    }

    public Either<RuntimeException, UserEntity> findById(UUID userId) {
        log.info("[Service: UserSearchUseCase] Iniciando a busca por id {}", userId);
        try {
            final UsersResource usersResource = keycloak.realm(properties.getRealm()).users();
            final var result = usersResource.get(userId.toString());

            if(result == null) {
                log.error("[Service: UserSearchUseCase] Usuário não encontrado {}", userId);
                return Either.left(new NotFoundException("Usuário não encontrado"));
            }

            log.info("[Service: UserSearchUseCase] Sucesso ao buscar o usuário");
            return Either.right(MapperUser.toEntity(result.toRepresentation()));
        } catch (RuntimeException e) {
            log.error("[Service: UserSearchUseCase] Falha ao tentar buscar o usuário {}", e.getMessage());
            return Either.left(new BadRequestException("Falha ao tentar buscar o usuário"));
        }
    }
}
