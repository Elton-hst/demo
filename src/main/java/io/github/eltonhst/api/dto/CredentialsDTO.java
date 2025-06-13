package io.github.eltonhst.api.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CredentialsDTO {

    private String email;

    private String username;

    private String password;

}
