package io.github.eltonhst.domain.useCase.auth;

import io.github.eltonhst.api.dto.AuthDTO;
import io.github.eltonhst.api.dto.UserDTO;
import io.github.eltonhst.domain.exception.NotAuthorizedException;
import io.github.eltonhst.domain.exception.UnauthorizedException;
import io.github.eltonhst.domain.useCase.user.UserSearchUseCase;
import io.github.eltonhst.infra.properties.KeyclockProperties;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthLoginUseCase {

    private final KeyclockProperties keyclockProperties;
    private final UserSearchUseCase searchUseCase;

    public AuthLoginUseCase(KeyclockProperties keyclockProperties, UserSearchUseCase searchUseCase) {
        this.keyclockProperties = keyclockProperties;
        this.searchUseCase = searchUseCase;
    }

    public Either<RuntimeException, AuthDTO> execute(UserDTO user) {
        try (var keycloakBuilder = KeycloakBuilder
                .builder()
                .realm(keyclockProperties.getRealm())
                .serverUrl(keyclockProperties.getAuthServerUrl())
                .clientId(keyclockProperties.getAdminClientId())
                .clientSecret(keyclockProperties.getAdminClientSecret())
                .grantType("password")
                .username(user.username())
                .password(user.password())
                .build()) {

            final String token = keycloakBuilder.tokenManager().getAccessTokenString();
            final String refreshToken = keycloakBuilder.tokenManager().getAccessToken().getRefreshToken();
            return searchUseCase.findByUsername(user.username()).fold(
                    Either::left,
                    userEntity -> Either.right(new AuthDTO(
                            token,
                            refreshToken,
                            userEntity
                    ))
            );

        } catch (NotAuthorizedException e) {
            return Either.left(new UnauthorizedException("Usuário ou senha inválidos"));
        }
    }

}
