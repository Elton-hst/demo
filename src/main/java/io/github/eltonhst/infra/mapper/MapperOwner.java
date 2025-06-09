package io.github.eltonhst.infra.mapper;

import io.github.eltonhst.api.dto.UserDTO;
import io.github.eltonhst.domain.entity.OwnerEntity;

public class MapperOwner {

    public static OwnerEntity toEntity(UserDTO user){
        return OwnerEntity.builder()
                .firstName(user.firstName())
                .lastName(user.lastName())
                .email(user.email())
                .password(user.password())
                .build();
    }

}
