package com.example.employeemanagement.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.util.UUID;

@Configuration
public class OAuth2ClientConfig {

    @Bean
    public CommandLineRunner registerAngularClient(
            RegisteredClientRepository registeredClientRepository) {

        return args -> {

            String clientId = "employee-management-angular";

            RegisteredClient existingClient =
                    registeredClientRepository.findByClientId(clientId);

            if (existingClient != null) {
                return;
            }

            RegisteredClient client = RegisteredClient
                    .withId(UUID.randomUUID().toString())
                    .clientId(clientId)
                    .clientName("Employee Management Angular")
                    .clientAuthenticationMethod(
                            ClientAuthenticationMethod.NONE
                    )
                    .authorizationGrantType(
                            AuthorizationGrantType.AUTHORIZATION_CODE
                    )
                    .authorizationGrantType(
                            AuthorizationGrantType.REFRESH_TOKEN
                    )
                    .redirectUri(
                            "http://localhost:4200/login/oauth2/code/angular"
                    )
                    .postLogoutRedirectUri(
                            "http://localhost:4200"
                    )
                    .scope("openid")
                    .scope("profile")
                    .build();

            registeredClientRepository.save(client);
        };
    }
}