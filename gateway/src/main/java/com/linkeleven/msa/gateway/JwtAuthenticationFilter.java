package com.linkeleven.msa.gateway;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class JwtAuthenticationFilter implements GlobalFilter {
  @Value("${service.jwt.secret-key}")
  private String secretKey;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String path = exchange.getRequest().getURI().getPath();
    if (path.equals("/auth/signIn")||path.equals("/auth/signUp")) {
      return chain.filter(exchange);  // /signIn 경로는 필터를 적용하지 않음
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
      SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
      Jws<Claims> claimsJws = Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token);
      log.info("#####body :: " + claimsJws.getBody().toString());
      Claims claims = checkValidateAuthService(claimsJws.getBody());
      exchange.getRequest().mutate()
          .header("X-User-Id", claims.get("user_id",String.class))
          .header("X-Role", claims.get("role",String.class))
          .build();
    } catch (SecurityException | MalformedJwtException | SignatureException e) {
      log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
    } catch (ExpiredJwtException e) {
      log.error("Expired JWT token, 만료된 JWT token 입니다.");
    } catch (UnsupportedJwtException e) {
      log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
    } catch (IllegalArgumentException e) {
      log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
    }
    return false;
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
