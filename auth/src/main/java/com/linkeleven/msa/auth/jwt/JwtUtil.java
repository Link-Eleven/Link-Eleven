package com.linkeleven.msa.auth.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.linkeleven.msa.auth.domain.common.UserRole;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
  private final Long accessExpiration;
  private final SecretKey secretKey;


  public JwtUtil(@Value("${service.jwt.secret-key}") String secretKey,
      @Value("${service.jwt.access-expiration}") Long accessExpiration) {
    this.accessExpiration = accessExpiration;
    this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));  // 생성자에서 key 초기화
  }

  public String createAccessToken(String userId, UserRole role) {
    String token =Jwts.builder()
        .claim("user_id", userId)
        .claim("role", role)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + accessExpiration))
        .signWith(secretKey, SignatureAlgorithm.HS512)
        .compact();
    return "Bearer "+token;
  }
}
