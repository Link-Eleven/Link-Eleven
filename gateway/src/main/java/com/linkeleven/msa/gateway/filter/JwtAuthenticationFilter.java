package com.linkeleven.msa.gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
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

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String path = exchange.getRequest().getURI().getPath();

    if (path.equals("/api/auth/signin")||path.equals("/api/auth/signup")) {;
      return chain.filter(exchange);
    }

    String token = extractToken(exchange);

    if (token == null || !validateToken(token,exchange)) {
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    }
    Claims claims = jwtProvider.parseToken(token);
    exchange = exchange.mutate()
        .request(exchange.getRequest().mutate()
            .header("X-User-Id", claims.get("user_id").toString())
            .header("X-Role", claims.get("role").toString())
            .build())
        .build();
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

      return true; // 토큰이 유효하면 true 반환
    } catch (CustomException e) {
      log.error("Authentication failed: {}", e.getMessage());
      return false; // 토큰이 유효하지 않으면 false 반환
    }
  }


/*  private void validateUserRoleUserService(Claims claims) {
    String role = claims.get("role").toString();
    long userId = Long.parseLong(claims.get("user_id").toString());

    if(role.equals("MASTER")||role.equals("COMPANY")) {
      UserRoleResponseDto userRoleResponseDto=userClient.getUserRole(userId);
      if(!userRoleResponseDto.getRole().equals(role)) {
        throw new CustomException(ErrorCode.ROLE_NOT_EQUALS);
      }
    }
  }*/

}
