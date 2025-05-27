package io.github.eltonhst.api.v1;

import io.github.eltonhst.api.dto.NewUserRecord;
import io.github.eltonhst.infra.security.KeycloakAdminConfig;
import io.vavr.control.Either;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final Keycloak keycloak;

    private final KeycloakAdminConfig keycloakAdminConfig;

    public UserController(Keycloak keycloak, KeycloakAdminConfig keycloakAdminConfig) {
        this.keycloak = keycloak;
        this.keycloakAdminConfig = keycloakAdminConfig;
    }

    //preciso passar a autenticação de client_credentials para password.
    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public Either<RuntimeException, UUID> createUser(@RequestBody NewUserRecord newUserRecord) {
        log.info("=== Start create user ===");

        var credentialRepresentation = preparePassword(newUserRecord.password());
        var userRepresentation = prepareUser(newUserRecord, credentialRepresentation);

        UsersResource usersResource = getUsersResource();
        try (Response response = usersResource.create(userRepresentation)) {
            log.info("=== Status Code {} ===", response.getStatus());

            if(response.getStatus() == 409){
                return Either.left(new RuntimeException("User already exists"));
            }

            if(response.getStatus() != 201){
                return Either.left(new RuntimeException("Erro: "+response.getEntity()));
            }

            final var location = response.getLocation();
            if (location == null) {
                throw new RuntimeException("Error getting id of created user");
            }

            Pattern pattern = Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");
            Matcher matcher = pattern.matcher(location.getPath());

            if (matcher.find()) {
                final var userId = UUID.fromString(matcher.group());
                log.info("=== New user has bee created ===");
                return Either.right(userId);
            }

            return Either.left(new RuntimeException("Formato inesperado da URL da location: "+location.getPath()));
        } catch (RuntimeException e) {
            log.error("=== Falha na criação do usuário ===");
            return Either.left(new RuntimeException("Erro ao criar usuário: " + e.getMessage()));
        }
    }

    @GetMapping()
    @PreAuthorize("hasRole('admin')")
    public UserRepresentation getUserById(Principal principal) {
        log.info("=== Start find user by id: {} ===", principal.getName());
        return getUsersResource().get(principal.getName()).toRepresentation();
    }

    private UsersResource getUsersResource(){
        return keycloak.realm(keycloakAdminConfig.getRealm()).users();
    }

    private void sendVerificationEmail(String id) {
        log.info("=== Sending verification email to id: {} ===", id);
        UsersResource usersResource = getUsersResource();
        usersResource.get(id).sendVerifyEmail();
    }

    private CredentialRepresentation preparePassword(String password) {
        log.info("=== Prepare password ===");
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);
        return credential;
    }

    private UserRepresentation prepareUser(NewUserRecord newUser, CredentialRepresentation password) {
        log.info("=== Prepare user ===");
        var user = new UserRepresentation();
        user.setUsername(newUser.username());
        user.setFirstName(newUser.firstName());
        user.setLastName(newUser.lastName());
        user.setEmail(newUser.email());
        user.setEnabled(true);
        user.setCredentials(List.of(password));
        return user;
    }

    private void addRoleToUser(UUID userId, String roleName) {
        log.info("=== Add role to user: {} ===", userId);
        final var realmResource = keycloak.realm(keycloakAdminConfig.getRealm());
        final var userResource = realmResource.users().get(userId.toString());
        final var role = realmResource.roles().get(roleName).toRepresentation();
        userResource.roles().realmLevel().add(List.of(role));
    }
}
