package io.github.eltonhst.domain.service.useCase.user;

import io.github.eltonhst.domain.entity.UserEntity;
import io.github.eltonhst.infra.repository.UserRepository;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class UserCreateUseCase {

    private final Keycloak keycloak;
    private final String realm = "spring-boot-code"; // Substitua pelo seu realm
    private final UserRepository repository;

    protected UserCreateUseCase(Keycloak keycloak, UserRepository repository) {
        this.keycloak = keycloak;
        this.repository = repository;
    }

    public String criarUsuario(String username, String email) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setEnabled(true);
        user.setEmailVerified(true);

        user.setAttributes(Collections.singletonMap("chave-personalizada", Collections.singletonList("valor")));

        Response response = keycloak.realm(realm)
                .users()
                .create(user);

        if (response.getStatus() == 201) {
            return "Usuário criado com sucesso!";
        } else {
            return "Erro ao criar usuário: " + response.getStatus();
        }
    }



}
