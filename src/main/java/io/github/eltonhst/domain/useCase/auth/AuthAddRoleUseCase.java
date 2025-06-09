package io.github.eltonhst.domain.useCase.auth;

import io.github.eltonhst.domain.exception.BadRequestException;
import io.github.eltonhst.domain.exception.InternalServerException;
import io.github.eltonhst.domain.exception.NotFoundException;
import io.github.eltonhst.infra.properties.KeyclockProperties;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class AuthAddRoleUseCase {

    private final Keycloak keycloak;
    private final KeyclockProperties keyclockProperties;

    public AuthAddRoleUseCase(Keycloak keycloak, KeyclockProperties keyclockProperties) {
        this.keycloak = keycloak;
        this.keyclockProperties = keyclockProperties;
    }

    public void execute(UUID userId, String roleName) {
        log.info("[Service: AuthAddRoleUseCase] Adicionando role {} ao usuário {}", roleName, userId);
        try  {
            final var realmResource = keycloak.realm(keyclockProperties.getRealm());

            final var userResource = realmResource.users().get(userId.toString());
            if(userResource.toRepresentation() == null) {
                log.error("[Service: AuthAddRoleUseCase] Usuário não encontrado");
                throw new NotFoundException("Usuário não encontrado");
            }

            final var role = realmResource.roles().get(roleName).toRepresentation();
            if(role == null) {
                log.error("[Service: AuthAddRoleUseCase] A role {} não existe", roleName);
                throw new BadRequestException("Role não existe no sistema");
            }

            userResource.roles().realmLevel().add(List.of(role));
        } catch (RuntimeException e) {
            log.error("[Service: AuthAddRoleUseCase] Falha ao tentar role ao usuário {}", userId);
            throw new InternalServerException(e.getMessage());
        }
    }

}
