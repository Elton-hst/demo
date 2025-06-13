package io.github.eltonhst.domain.validation;

import io.github.eltonhst.domain.entity.OwnerEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.util.stream.Collectors.joining;

@Slf4j
public class ValidationOwnerImpl implements Validator<OwnerEntity>{

    private String campo;

    @Override
    public Map<String, String> validate(OwnerEntity ownerEntity) {
        log.info("[Validation] Iniciando a validação do owner");
        Map<String, String> errors = new HashMap<>();
        validateId(ownerEntity.getId(), errors);
        validateName(ownerEntity.getFirstName(), errors);
        validateName(ownerEntity.getLastName(), errors);
        validateEmail(ownerEntity.getEmail(), errors);
        validatePassword(ownerEntity.getPassword(), errors);
        if (!errors.isEmpty()) {
            var message = errors.entrySet()
                    .stream()
                    .map(e -> e.getKey() + ": " + e.getValue())
                    .collect(joining(", "));
            log.error("[Validation] Errors: {}", message);
        }
        log.info("[Validation] Sucesso ao validar o owner");
        return errors;
    }

    private void validateId(UUID id, Map<String, String> errors) {
        campo = "ID";
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
