package io.github.eltonhst.infra.mapper;

import io.github.eltonhst.api.dto.UserDTO;
import io.github.eltonhst.domain.entity.ClientEntity;

public class MapperClient {

    public static ClientEntity toEntity(UserDTO user){
        return ClientEntity.builder()
                .firstName(user.firstName())
                .lastName(user.lastName())
                .email(user.email())
                .password(user.password())
                .build();
    }

}
