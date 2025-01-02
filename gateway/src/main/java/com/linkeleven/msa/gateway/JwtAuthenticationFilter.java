package com.linkeleven.msa.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.linkeleven.msa.gateway.libs.exception.CustomException;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class JwtAuthenticationFilter implements GlobalFilter {
  private final JwtProvider jwtProvider;

  @Autowired
  public JwtAuthenticationFilter(JwtProvider jwtProvider) {
    this.jwtProvider = jwtProvider;
  }

  @Value("${service.jwt.secret-key}")
  private String secretKey;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String path = exchange.getRequest().getURI().getPath();
    if (path.startsWith("/api/auth/")) {
      return chain.filter(exchange);
    }

    String token = extractToken(exchange);

    if (token == null || !validateToken(token,exchange)) {
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    }

    return chain.filter(exchange);
  }

  private String extractToken(ServerWebExchange exchange) {
    String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      return authHeader.substring(7);
    }
    return null;
  }

  public boolean validateToken(String token,ServerWebExchange exchange) {
    try {
      Claims claims = jwtProvider.parseToken(token);
      Claims validatedClaims = jwtProvider.validateClaims(claims);

      exchange.getRequest().mutate()
          .header("X-User-Id", validatedClaims.get("user_id", String.class))
          .header("X-Role", validatedClaims.get("role", String.class))
          .build();

      return true; // 토큰이 유효하면 true 반환
    } catch (CustomException e) {
      log.error("Authentication failed: {}", e.getMessage());
      return false; // 토큰이 유효하지 않으면 false 반환
    }
  }


  private Claims checkValidateAuthService(Claims body) {
    String userId = body.get("user_id",String.class);
    String role = body.get("role",String.class);

    if(userId!=null){
      //if(auth check)  false -> 에러 반환
      return body;
    }
    if(role!=null){
      //if(auth check) 권한 같은지 확인 후 반환
      return body;
    }
    return body;
  }

}
