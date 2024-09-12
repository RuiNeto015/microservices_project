package com.icligo.voyage.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PredicateFactoryConfig {

  @Bean
  public UserAgentRoutePredicateFactory userAgent() {
    return new UserAgentRoutePredicateFactory();
  }

}
