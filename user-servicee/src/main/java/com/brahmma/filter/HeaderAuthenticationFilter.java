package com.brahmma.filter;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

@Component
public class HeaderAuthenticationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String username = request.getHeader("X-User-Name");
		String rolesHeader = request.getHeader("X-User-Roles");

		if (username != null) {

			List<GrantedAuthority> authorities = new ArrayList<>();

			if (rolesHeader != null) {
				for (String role : rolesHeader.split(",")) {
					authorities.add(new SimpleGrantedAuthority(role.trim()));
				}
			}

			Authentication auth = new UsernamePasswordAuthenticationToken(username, null, authorities);

			SecurityContextHolder.getContext().setAuthentication(auth);
		}

		filterChain.doFilter(request, response);
	}
}
