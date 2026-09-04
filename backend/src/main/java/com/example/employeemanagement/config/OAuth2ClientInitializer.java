package com.example.employeemanagement.config;

import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;

@Configuration
public class OAuth2ClientInitializer {

    @Bean
    CommandLineRunner initializeOAuth2Client(
            RegisteredClientRepository registeredClientRepository) {

        return args -> {

            String clientId = "employee-management-angular";

            RegisteredClient existingClient =
                    registeredClientRepository.findByClientId(clientId);

            if (existingClient == null) {

                RegisteredClient angularClient =
                        RegisteredClient.withId(
                                UUID.randomUUID().toString()
                        )
                        .clientId(clientId)

                        // Development only
                        .clientSecret("{noop}dev-secret")

                        .clientAuthenticationMethod(
                                ClientAuthenticationMethod.CLIENT_SECRET_BASIC
                        )

                        .clientAuthenticationMethod(
                                ClientAuthenticationMethod.NONE
                        )
                        .clientSettings(
                                ClientSettings.builder()
                                        .requireProofKey(true)
                                        .requireAuthorizationConsent(true)
                                        .build()
                        )

                        .authorizationGrantType(
                                AuthorizationGrantType.AUTHORIZATION_CODE
                        )

                        .authorizationGrantType(
                                AuthorizationGrantType.REFRESH_TOKEN
                        )

                        .redirectUri(
                                "http://localhost:4200/login/oauth2/code/employee-management-angular"
                        )

                        .postLogoutRedirectUri(
                                "http://localhost:4200/"
                        )

                        .scope(OidcScopes.OPENID)
                        .scope(OidcScopes.PROFILE)

                        .build();

                registeredClientRepository.save(angularClient);

                System.out.println(
                        "OAuth2 Angular client registered successfully."
                );
            }
        };
    }
}