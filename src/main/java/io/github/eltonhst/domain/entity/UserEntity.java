package io.github.eltonhst.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class UserEntity {

    private UUID userId;

    private String firstName;

    private String lastName;

    private String username;

    private String password;

    private String email;

    private boolean isOwner;

    private List<String> roles;

}
