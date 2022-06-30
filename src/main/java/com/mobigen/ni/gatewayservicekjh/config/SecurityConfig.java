package com.mobigen.ni.gatewayservicekjh.config;

import com.mobigen.ni.gatewayservicekjh.interceptor.InterceptorConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Autowired
    private InterceptorConfig interceptorConfig;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .addFilterBefore(interceptorConfig, SecurityWebFiltersOrder.AUTHENTICATION)
                .authorizeExchange()
                .pathMatchers("/realms/*/protocol/openid-connect/token").permitAll()
                .pathMatchers("/webClientTest").permitAll()
                .and()
                .authorizeExchange()
                .anyExchange().authenticated()
                .and()
                .oauth2Login()
                .and()
                .oauth2ResourceServer()
                .jwt();
        http.csrf().disable();
        return http.build();
    }
}