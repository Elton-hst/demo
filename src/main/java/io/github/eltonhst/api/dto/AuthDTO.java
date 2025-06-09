package io.github.eltonhst.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthDTO {

    private String token;

    private String refreshToken;

    private Object profile;

}
