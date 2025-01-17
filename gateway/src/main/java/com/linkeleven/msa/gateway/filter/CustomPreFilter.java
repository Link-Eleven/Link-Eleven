package com.linkeleven.msa.gateway.filter;

// @Component
// @Slf4j
// public class CustomPreFilter implements GlobalFilter, Ordered {
//
//   @Override
//   public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//     ServerHttpRequest response = exchange.getRequest();
//     log.info("Pre Filter: Request URI is " + response.getURI());
//     // Add any custom logic here
//
//     return chain.filter(exchange);
//   }
//
//   @Override
//   public int getOrder() {
//     return Ordered.HIGHEST_PRECEDENCE;
//   }
// }
