package com.qc.printers.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "cas")
public class CASConfig {
    private String baseUrl;

    private String clientId;

    private String clientSecret;
}
