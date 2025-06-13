package io.github.eltonhst.domain.validation;

import io.github.eltonhst.domain.entity.ClientEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.util.stream.Collectors.joining;

@Slf4j
public class ValidationClientImpl implements Validator<ClientEntity>{

    private String campo;

    @Override
    public Map<String, String> validate(ClientEntity clientEntity) {
        log.info("[Validation] iniciando a validação do client");
        Map<String, String> errors = new HashMap<>();
        validateId(clientEntity.getId(), errors);
        validateUserId(clientEntity.getUserId(), errors);
        validateName(clientEntity.getFirstName(), errors);
        validateName(clientEntity.getLastName(), errors);
        validateEmail(clientEntity.getEmail(), errors);
        validatePassword(clientEntity.getPassword(), errors);
        if (!errors.isEmpty()) {
            var message = errors.entrySet()
                    .stream()
                    .map(e -> e.getKey() + ": " + e.getValue())
                    .collect(joining(", "));
            log.error("[Validation] Errors: {}", message);
        }
        log.info("[Validation] Sucesso ao validar o client");
        return errors;
    }

    private void validateId(UUID id, Map<String, String> errors) {
        campo = "ID";
        if(id == null) {
            errors.put(campo, "não pode ser nulo");
        }
    }

    private void validateUserId(UUID id, Map<String, String> errors) {
        campo = "USER ID";
        if(id == null) {
            errors.put(campo, "não pode ser nulo");
        }
    }

    private void validateName(String name, Map<String, String> errors) {
        campo = "NAME";
        if(name.isEmpty()) {
            errors.put(campo, "não pode ser nulo");
        }
    }

    private void validateEmail(String email, Map<String, String> errors) {
        campo = "EMAIL";
        if(email.isEmpty()) {
            errors.put(campo, "não pode ser nulo");
        }
    }

    private void validatePassword(String password, Map<String, String> errors) {
        campo = "PASSWORD";
        if(password.isEmpty()) {
            errors.put(campo, "não pode ser nulo");
        }
    }
}
