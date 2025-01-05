package com.linkeleven.msa.feed.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class TokenVerifier {

	@Value("${service.jwt.secret-key}")
	private String secretKey;

	public Claims getClaimsFromToken(String token) {
		String actualToken = token.replace("Bearer ", "");
		return Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(actualToken).getBody();
	}

	public Long getUserIdFromToken(String token) {
		Claims claims = getClaimsFromToken(token);
		String userIdtoString = claims.get("user_id", String.class);
		return Long.parseLong(userIdtoString);
	}

	public String getRoleFromToken(String token) {
		Claims claims = getClaimsFromToken(token);
		return claims.get("role", String.class);
	}
}
