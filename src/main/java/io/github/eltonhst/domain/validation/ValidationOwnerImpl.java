package io.github.eltonhst.domain.validation;

import io.github.eltonhst.domain.entity.OwnerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ValidationOwnerImpl implements Validator<OwnerEntity>{

    @Override
    public Map<String, String> validate(OwnerEntity ownerEntity) {
        Map<String, String> errors = new HashMap<>();
        validateId(ownerEntity.getId(), errors);
        validateName(ownerEntity.getFirstName(), errors);
        validateName(ownerEntity.getLastName(), errors);
        validateEmail(ownerEntity.getEmail(), errors);
        validatePassword(ownerEntity.getPassword(), errors);
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
