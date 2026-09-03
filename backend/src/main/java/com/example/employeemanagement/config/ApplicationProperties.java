package com.example.employeemanagement.config;

import org.springframework.boot.context.properties.ConfigurationProperties;






@ConfigurationProperties(prefix = "app")
public class ApplicationProperties {
    private String issuerUrl = "http://localhost:8082";

    public String getIssuerUrl() {
        return issuerUrl;
    }

    public void setIssuerUrl(String issuerUrl) {
        this.issuerUrl = issuerUrl;
    }
}
