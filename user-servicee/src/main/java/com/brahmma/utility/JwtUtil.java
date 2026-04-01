package com.brahmma.utility;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.brahmma.security.UserDetailsImpl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private final String SECRET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456";

	private Key getSigningKey() {
		return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
	}

	public String generateToken(UserDetails userDetails) {

		UserDetailsImpl user = (UserDetailsImpl) userDetails;

		Map<String, Object> claims = new HashMap<>();
		claims.put("roles",
				user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

		return Jwts.builder().setClaims(claims).setSubject(user.getUsername()).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 86400000))
				.signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
	}
}
