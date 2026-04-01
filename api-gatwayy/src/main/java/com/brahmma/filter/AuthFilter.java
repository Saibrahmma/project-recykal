package com.brahmma.filter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import com.brahmma.util.JwtUtil;
import io.jsonwebtoken.Claims;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    @Autowired
    private JwtUtil jwtUtil;

    public AuthFilter() {
        super(Config.class);
    }

    public static class Config {}

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {

            // Allow preflight
            if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
                return chain.filter(exchange);
            }

            String path = exchange.getRequest().getURI().getPath();

            // Allow /auth/**
            if (path.startsWith("/auth/")) {
                return chain.filter(exchange);
            }

            // Check Authorization header
            String authHeader = exchange.getRequest()
                    .getHeaders()
                    .getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String token = authHeader.substring(7);

            Claims claims;
            try {
                jwtUtil.validateToken(token);
                claims = jwtUtil.extractAllClaims(token);
            } catch (Exception e) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            // EXTRACT ROLES
            List<String> roles = claims.get("roles", List.class);

            // Forward username + roles
            ServerHttpRequest modifiedRequest = exchange.getRequest()
                    .mutate()
                    .header("X-User-Name", claims.getSubject())
                    .header("X-User-Roles", String.join(",", roles))   // IMPORTANT
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        };
    }
}
