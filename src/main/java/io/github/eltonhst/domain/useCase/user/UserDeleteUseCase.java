package io.github.eltonhst.domain.useCase.user;

import io.github.eltonhst.domain.exception.BadRequestException;
import io.github.eltonhst.infra.properties.KeyclockProperties;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class UserDeleteUseCase {

    private final Keycloak keycloak;
    private final KeyclockProperties properties;

    public UserDeleteUseCase(Keycloak keycloak, KeyclockProperties properties) {
        this.keycloak = keycloak;
        this.properties = properties;
    }

    public void execute(UUID userId) {
        log.info("[Service] Iniciando a deleção do usuário {}", userId);

        String userIdStr = userId.toString();

        UsersResource usersResource = keycloak
                .realm(properties.getRealm())
                .users();

        List<UserRepresentation> users = usersResource.search(userIdStr, true);

        try {
            users.stream()
                    .filter(user -> user.getId().equals(userIdStr))
                    .findFirst()
                    .ifPresent(user -> usersResource.delete(user.getId()));

            log.info("[Service] Deleção do usuário realizada com sucesso");
        } catch (RuntimeException e) {
            log.error("[Service] Erro ao tentar excluir o usuário");
            throw new BadRequestException(e.getMessage());
        }
    }

}
