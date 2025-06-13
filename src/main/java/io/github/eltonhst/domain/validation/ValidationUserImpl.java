package io.github.eltonhst.domain.validation;

import io.github.eltonhst.api.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.joining;

@Slf4j
public class ValidationUserImpl implements Validator<UserDTO>{

    private String campo;

    @Override
    public Map<String, String> validate(UserDTO userDTO) {
        log.info("[Validation] Iniciando a validação do usuário");
        Map<String, String> errors = new HashMap<>();
        validateName(userDTO.firstName(), errors);
        validateName(userDTO.lastName(), errors);
        validateName(userDTO.username(), errors);
        validateEmail(userDTO.email(), errors);
        validatePassword(userDTO.password(), errors);
        if (!errors.isEmpty()) {
            var message = errors.entrySet()
                    .stream()
                    .map(e -> e.getKey() + ": " + e.getValue())
                    .collect(joining(", "));
            log.error("[Validation] Errors: {}", message);
        }
        log.info("[Validation] Sucesso ao validar o usuário");
        return errors;
    }

    private void validatePassword(String password, Map<String, String> errors) {
        campo = "PASSWORD";
        if(password.isEmpty()) {
            errors.put(campo, "não pode ser nulo");
        }
    }

    private void validateEmail(String email, Map<String, String> errors) {
        campo = "EMAIL";
        if(email.isEmpty()) {
            errors.put(campo, "não pode ser nulo");
        }
    }

    private void validateName(String name, Map<String, String> errors) {
        campo = "NAME";
        if(name.isEmpty()) {
            errors.put(campo, "não pode ser nulo");
        }
    }
}
