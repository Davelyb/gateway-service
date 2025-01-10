package com.example.gateway.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final WebClient webClient;

    public AuthService(@LoadBalanced WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("lb://auth-service").build();
    }

    public Mono<UserInfo> validateToken(String token) {
        return webClient.get()
                .uri("/api/users/gateway-auth")
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(UserInfo.class)
                .doOnNext(userInfo -> 
                    log.info("Authentication success - userId: {}, username: {}", 
                            userInfo.getUserId(), userInfo.getUsername())
                )
                .doOnError(error -> 
                    log.error("Authentication failed: {}", error.getMessage())
                );
    }

    public static class UserInfo {
        @JsonProperty("user_id")
        private Long userId;
        private String username;

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}