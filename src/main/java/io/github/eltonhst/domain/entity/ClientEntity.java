package io.github.eltonhst.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@Entity
public class ClientEntity {

    @Id
    private UUID id;

    private UUID userId;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

}
