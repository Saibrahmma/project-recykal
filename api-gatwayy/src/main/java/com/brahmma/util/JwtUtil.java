package com.brahmma.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private final String SECRET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456";

	private Key getSigningKey() {
		return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
	}

	public Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
	}

	public void validateToken(String token) {
		// will throw exceptions if invalid/expired
		Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
	}
}
