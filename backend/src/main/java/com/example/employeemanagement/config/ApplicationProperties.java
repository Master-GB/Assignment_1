package com.example.employeemanagement.config;

import org.springframework.boot.context.properties.ConfigurationProperties;






@ConfigurationProperties(prefix = "app")
public class ApplicationProperties {
    /**
     * Issuer URL for the Authorization Server.
     * Default is http://localhost:8080.
     */
    private String issuerUrl = "http://localhost:8080";

    public String getIssuerUrl() {
        return issuerUrl;
    }

    public void setIssuerUrl(String issuerUrl) {
        this.issuerUrl = issuerUrl;
    }
}
