package com.brahmma.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.brahmma.filter.AuthFilter;

@Configuration
public class GatewayConfig {

	@Bean
	public RouteLocator routeLocator(RouteLocatorBuilder builder, AuthFilter authFilter) {
		return builder.routes()

				// user Service (no token needed)
				.route("user-auth", r -> r.path("/auth/**").uri("lb://user-service"))

				// user Service
				.route("user-admin",
						r -> r.path("/admin/**").filters(f -> f.filter(authFilter.apply(new AuthFilter.Config())))
								.uri("lb://user-service"))

				// facility Service
				.route("facility-service",
						r -> r.path("/facility/**").filters(f -> f.filter(authFilter.apply(new AuthFilter.Config())))
								.uri("lb://facility-service"))

				// asset Service
				.route("asset-service",
						r -> r.path("/asset/**").filters(f -> f.filter(authFilter.apply(new AuthFilter.Config())))
								.uri("lb://asset-service"))
				
				// workorder Service
				.route("workorder-service",
						r -> r.path("/workorder/**").filters(f -> f.filter(authFilter.apply(new AuthFilter.Config())))
								.uri("lb://workorder-service"))

				.build();
	}
}
