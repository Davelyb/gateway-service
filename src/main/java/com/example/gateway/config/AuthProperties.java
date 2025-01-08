package com.example.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "gateway.auth")
public class AuthProperties {
    private List<PathConfig> excludePaths = new ArrayList<>();

    public List<PathConfig> getExcludePaths() {
        return excludePaths;
    }

    public void setExcludePaths(List<PathConfig> excludePaths) {
        this.excludePaths = excludePaths;
    }

    public static class PathConfig {
        private String path;
        private List<String> methods = new ArrayList<>();

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public List<String> getMethods() {
            return methods;
        }

        public void setMethods(List<String> methods) {
            this.methods = methods;
        }
    }
} 