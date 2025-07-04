package io.github.eltonhst.infra.mapper;

import io.github.eltonhst.api.dto.UserDTO;
import io.github.eltonhst.domain.entity.UserEntity;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.UUID;

public class MapperUser {

    public static UserEntity toEntity(UserRepresentation data) {
        return UserEntity.builder()
                .userId(UUID.fromString(data.getId()))
                .username(data.getUsername())
                .email(data.getEmail())
                .firstName(data.getFirstName())
                .lastName(data.getLastName())
                .enabled(data.isEnabled())
                .emailVerified(data.isEmailVerified())
                .createdTimestamp(data.getCreatedTimestamp())
                .build();
    }

    public static UserEntity toEntity(UserDTO userDTO, UUID userId) {
        return UserEntity.builder()
                .userId(userId)
                .firstName(userDTO.firstName())
                .lastName(userDTO.lastName())
                .password(userDTO.password())
                .email(userDTO.email())
                .build();
    }

    public static UserDTO toDTO(UserEntity result) {
        return UserDTO.builder()
                .firstName(result.getFirstName())
                .lastName(result.getLastName())
                .password(result.getPassword())
                .email(result.getEmail())
                .isOwner(false)
                .build();
    }
}
