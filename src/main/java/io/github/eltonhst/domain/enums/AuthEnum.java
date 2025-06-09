package io.github.eltonhst.domain.enums;

import lombok.Getter;

@Getter
public enum AuthEnum {
    OWNER("owner"),
    CLIENT("client");

    private final String role;

    AuthEnum(String role) {
        this.role = role;
    }

}
