package com.stromae.gatewayservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                // Video Service Routes
                .route("video-service", r -> r
                        .path("/api/videos/**")
                        .uri("lb://video-service"))

                // User Service Routes
                .route("user-service", r -> r
                        .path("/api/users/**", "/api/watchlist/**", "/api/watch-history/**")
                        .uri("lb://user-service"))

                // Security Service Routes
                .route("security-service", r -> r
                        .path("/api/auth/**")
                        .uri("lb://security-service"))

                // Monitoring Service Routes
                .route("monitoring-service", r -> r
                        .path("/api/monitoring/**")
                        .uri("lb://monitoring-service"))

                .build();
    }

}
