package io.github.eltonhst.domain.validation;

import io.github.eltonhst.domain.exception.ValidatorException;
import java.util.Map;

import static java.util.stream.Collectors.joining;

public interface Validator<T> {

    Map<String,String> validate(T t);

    static <T> void validate(Validator<T> validator, T t) {
        final var errors = validator.validate(t);

        if(!errors.isEmpty()) {
            var message = errors
                    .entrySet()
                    .stream()
                    .map(e-> e.getKey() + ": " + e.getValue())
                    .collect(joining(", "));
            throw new ValidatorException("[VALIDATION EXCEPTION]: " + message);
        }

    }

}
