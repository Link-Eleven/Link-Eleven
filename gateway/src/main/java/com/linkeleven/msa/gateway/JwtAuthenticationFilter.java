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

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String path = exchange.getRequest().getURI().getPath();

    if (path.equals("/api/auth/signin")||path.equals("/api/auth/signup")) {;
      return chain.filter(exchange);  // /signIn 경로는 필터를 적용하지 않음
    }

    String token = extractToken(exchange);

    if (token == null || !validateToken(token,exchange)) {
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    }
    Claims claims = jwtProvider.parseToken(token);

    exchange = exchange.mutate()
        .request(exchange.getRequest().mutate()
            .header("X-User-Id", claims.get("user_id").toString())  // 원하는 값으로 설정
            .header("X-Role", claims.get("role").toString())     // 원하는 값으로 설정
            .build())  // 새로운 요청 객체를 생성
        .build();  // 변경된 요청 객체를 exchange에 반영
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
