package io.github.eltonhst.domain.validation;

import io.github.eltonhst.domain.entity.ClientEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ValidationClientImpl implements Validator<ClientEntity>{

    @Override
    public Map<String, String> validate(ClientEntity clientEntity) {
        Map<String, String> errors = new HashMap<>();
        validateId(clientEntity.getId(), errors);
        validateName(clientEntity.getFirstName(), errors);
        validateName(clientEntity.getLastName(), errors);
        validateEmail(clientEntity.getEmail(), errors);
        validatePassword(clientEntity.getPassword(), errors);
        return errors;
    }

    private void validateId(UUID id, Map<String, String> errors) {
        String campo = "ID: ";
        if(id == null) {
            errors.put(campo, "não pode ser nulo");
        }
    }

    private void validateName(String name, Map<String, String> errors) {
        String campo = "NAME: ";
        if(name.isEmpty()) {
            errors.put(campo, "não pode ser nulo");
        }
    }

    private void validateEmail(String email, Map<String, String> errors) {
        String campo = "EMAIL: ";
        if(email.isEmpty()) {
            errors.put(campo, "não pode ser nulo");
        }
    }

    private void validatePassword(String password, Map<String, String> errors) {
        String campo = "PASSWORD: ";
        if(password.isEmpty()) {
            errors.put(campo, "não pode ser nulo");
        }
    }
}
