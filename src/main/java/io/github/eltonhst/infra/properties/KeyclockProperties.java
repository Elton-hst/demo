package io.github.eltonhst.infra.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class KeyclockProperties {

    @Value("${keycloak.adminClientId}")
    private String adminClientId;

    @Value("${keycloak.adminClientSecret}")
    private String adminClientSecret;

    @Value("${keycloak.urls.auth}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

}
