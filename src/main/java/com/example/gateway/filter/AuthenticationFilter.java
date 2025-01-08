package com.example.gateway.filter;

import com.example.gateway.config.AuthProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

    @Autowired
    private AuthProperties authProperties;
    
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();
        HttpMethod method = request.getMethod();
        
        System.out.println("path: " + path);
        System.out.println("method: " + method);

        // 检查是否在白名单中
        if (isExcludedPath(path, method)) {
            return chain.filter(exchange);
        }

        String authHeader = request.getHeaders().getFirst("Authorization");
        System.out.println("authHeader: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            exchange.getResponse().getHeaders().add("Content-Type", "application/json");
            String errorMessage = "{\"message\":\"认证失败:请提供有效的Bearer Token\",\"status\":401}";
            byte[] bytes = errorMessage.getBytes();
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
        }
        
        return chain.filter(exchange);
    }

    private boolean isExcludedPath(String path, HttpMethod method) {
        return authProperties.getExcludePaths().stream()
                .filter(config -> pathMatcher.match(config.getPath(), path))
                .anyMatch(config -> {
                    // 如果methods为空，表示所有方法都允许
                    if (config.getMethods() == null || config.getMethods().isEmpty()) {
                        return true;
                    }
                    // 否则检查具体方法是否匹配
                    return config.getMethods().contains(method.name());
                });
    }

    @Override
    public int getOrder() {
        return -100;
    }
} 