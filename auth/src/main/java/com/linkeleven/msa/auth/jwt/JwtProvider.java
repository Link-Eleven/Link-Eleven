package com.linkeleven.msa.auth.jwt;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.linkeleven.msa.auth.libs.exception.CustomException;
import com.linkeleven.msa.auth.libs.exception.ErrorCode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtProvider {

	private final SecretKey secretKey;

	public JwtProvider(@Value("${service.jwt.secret-key}") String secretKey) {
		this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
	}
	public String validateToken(String token) {
		if(token==null){
			throw new CustomException(ErrorCode.INVALID_JWT_CLAIMS);
		}
		if (token.startsWith("Bearer ")) {
			token = token.substring(7);
		}

		return parseToken(token);
	}

	private String parseToken(String token) {
		try {
			Jws<Claims> claimsJws = Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token);
			return claimsJws.getBody().get("user_id",String.class);
		} catch (SecurityException | MalformedJwtException | SignatureException e) {
			log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
			throw new CustomException(ErrorCode.INVALID_JWT_SIGNATURE);
		} catch (ExpiredJwtException e) {
			log.error("Expired JWT token, 만료된 JWT token 입니다.");
			throw new CustomException(ErrorCode.EXPIRED_JWT_TOKEN);
		} catch (UnsupportedJwtException e) {
			log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
			throw new CustomException(ErrorCode.UNSUPPORTED_JWT_TOKEN);
		} catch (IllegalArgumentException e) {
			log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
			throw new CustomException(ErrorCode.INVALID_JWT_CLAIMS);
		}
	}
}
