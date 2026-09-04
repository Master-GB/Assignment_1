package com.example.employeemanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.util.List;
import org.springframework.security.core.GrantedAuthority;


@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * Authorization Server Security
     */
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(
            HttpSecurity http) throws Exception {

        OAuth2AuthorizationServerConfigurer authorizationServer =
                new OAuth2AuthorizationServerConfigurer();

        http
            .cors(Customizer.withDefaults())
            // IMPORTANT:
            // This chain only handles OAuth2 Authorization Server endpoints
            .securityMatcher(
                    authorizationServer.getEndpointsMatcher()
            )

            .with(
                    authorizationServer,
                    configurer -> configurer
                            .oidc(Customizer.withDefaults())
                            .authorizationEndpoint(authorizationEndpoint ->
                                    authorizationEndpoint.consentPage(null)
                            )
            )

            .authorizeHttpRequests(authorize -> authorize
                    .anyRequest().authenticated()
            )

            // Redirect unauthenticated OAuth2 requests to /login
            .exceptionHandling(exceptionHandling -> exceptionHandling
                    .authenticationEntryPoint(
                            new LoginUrlAuthenticationEntryPoint("/login")
                    )
            );

        return http.build();
    }


    @Bean
    @Order(2)
    public SecurityFilterChain applicationSecurityFilterChain(
            HttpSecurity http,
            JwtAuthenticationConverter jwtAuthenticationConverter)
            throws Exception {

        http
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(authorize -> authorize

                // Public authentication endpoints
                .requestMatchers("/api/auth/register", "/login")
                .permitAll()

                // Everything else requires authentication
                .anyRequest()
                .authenticated()
            )

            // REST APIs should not use CSRF protection
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**")
            )

            // Custom login page configuration
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("http://localhost:4200/dashboard", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )

            // Logout configuration
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            )

            // JWT authentication for API requests
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(
                        jwtAuthenticationConverter
                    )
                )
            );

        return http.build();
    }

    /**
     * Password Encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

   @Bean
   public JwtAuthenticationConverter jwtAuthenticationConverter() {

    JwtAuthenticationConverter converter =
            new JwtAuthenticationConverter();

    converter.setJwtGrantedAuthoritiesConverter(jwt -> {

        List<String> roles =
                jwt.getClaimAsStringList("roles");

        if (roles == null) {
            return List.of();
        }

        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .map(authority -> (GrantedAuthority) authority)
                .toList();
    });

    return converter;
}
}