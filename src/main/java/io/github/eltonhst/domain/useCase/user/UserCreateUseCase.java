package io.github.eltonhst.domain.useCase.user;

import io.github.eltonhst.api.dto.UserDTO;
import io.github.eltonhst.domain.useCase.client.ClientCreateUseCase;
import io.github.eltonhst.domain.useCase.owner.OwnerCreateUseCase;
import io.github.eltonhst.infra.mapper.MapperUser;
import io.github.eltonhst.infra.properties.KeyclockProperties;
import io.vavr.control.Either;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class UserCreateUseCase {

    private final Keycloak keycloak;
    private final KeyclockProperties properties;
    private final OwnerCreateUseCase ownerCreateUseCase;
    private final ClientCreateUseCase clientCreateUseCase;

    protected UserCreateUseCase(Keycloak keycloak, KeyclockProperties properties, OwnerCreateUseCase ownerCreateUseCase, ClientCreateUseCase clientCreateUseCase) {
        this.keycloak = keycloak;
        this.properties = properties;
        this.ownerCreateUseCase = ownerCreateUseCase;
        this.clientCreateUseCase = clientCreateUseCase;
    }

    public Either<RuntimeException, UUID> execute(@RequestBody UserDTO userDTO) {
        log.info("[Service: UserCreateUseCase] Iniciando a criação do usuário {}", userDTO.username());
        var credentialRepresentation = preparePassword(userDTO.password());
        var userRepresentation = prepareUser(userDTO, credentialRepresentation);

        final UsersResource usersResource = keycloak.realm(properties.getRealm()).users();
        try (Response response = usersResource.create(userRepresentation)) {

            if(response.getStatus() == 409){
                log.error("[Service: UserCreateUseCase] Usuário já existe");
                return Either.left(new RuntimeException("Usuário já existe"));
            }

            if(response.getStatus() != 201){
                log.error("[Service: UserCreateUseCase] Erro inesperado");
                return Either.left(new RuntimeException("Erro inesperado: "+response.getEntity()));
            }

            final var location = response.getLocation();
            if (location == null) {
                log.error("[Service: UserCreateUseCase] Id do usuário não encontrado");
                return Either.left(new RuntimeException("Erro ao buscar o id do usuário criado"));
            }

            Pattern pattern = Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");
            Matcher matcher = pattern.matcher(location.getPath());

            if (matcher.find()) {
                final var userId = UUID.fromString(matcher.group());
                return creatingUserInDatabase(userDTO, userId);
            }

            return Either.left(new RuntimeException("Formato inesperado da URL da location: " + location.getPath()));
        } catch (RuntimeException e) {
            return Either.left(new RuntimeException("Erro ao criar usuário: " + e.getMessage()));
        }
    }

    private Either<RuntimeException, UUID> creatingUserInDatabase(UserDTO userDTO, UUID userId) {
        if (userDTO.isOwner()) {
            return ownerCreateUseCase.execute(MapperUser.toEntity(userDTO, userId));
        } else {
            return clientCreateUseCase.execute(MapperUser.toEntity(userDTO, userId));
        }
    }

    private CredentialRepresentation preparePassword(String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);
        return credential;
    }

    private UserRepresentation prepareUser(UserDTO newUser, CredentialRepresentation password) {
        var user = new UserRepresentation();
        user.setUsername(newUser.username());
        user.setFirstName(newUser.firstName());
        user.setLastName(newUser.lastName());
        user.setEmail(newUser.email());
        user.setEnabled(true);
        user.setCredentials(List.of(password));
        return user;
    }

}
