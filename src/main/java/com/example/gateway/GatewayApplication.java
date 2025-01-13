package com.example.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

    @Value("${spring.cloud.gateway.discovery.locator.enabled}")
    private String locatorEnabled;

    @GetMapping("/config")
    public String config() {
        return "locatorEnabled: " + locatorEnabled;
    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
} 