package com.icligo.voyage.gateway.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ServerWebExchange;

import java.util.function.Predicate;

@Slf4j
@Component
public class UserAgentRoutePredicateFactory extends AbstractRoutePredicateFactory<UserAgentRoutePredicateFactory.Config> {

    public UserAgentRoutePredicateFactory() {
        super(Config.class);
    }

    private Boolean matches(Config config, ServerHttpRequest request) {
        String userAgent = request.getHeaders().getFirst("User-Agent");
        boolean isMobile = StringUtils.hasText(userAgent) && userAgent.toLowerCase().contains("mobile");
        log.info("Request from supplier: {}, User-Agent: {}, isMobile: {}", config.getValue(), userAgent, isMobile);
        return isMobile;
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return exchange -> {
            ServerHttpRequest request = exchange.getRequest();
            return matches(config, request);
        };
    }

    @Validated
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Config {
        String value;
    }
}
