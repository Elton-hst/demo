package io.github.eltonhst.api.dto;

import lombok.Builder;

@Builder
public record UserDTO(String username,String password,String firstName, String lastName, String email,
                            boolean isOwner) {
}
