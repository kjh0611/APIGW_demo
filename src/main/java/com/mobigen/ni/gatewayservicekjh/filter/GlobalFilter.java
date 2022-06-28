package com.mobigen.ni.gatewayservicekjh.filter;

import com.mobigen.ni.gatewayservicekjh.config.WebClientConfig;
import com.mobigen.ni.gatewayservicekjh.controller.ApiTestController;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {

    private final ModifyResponseBodyGatewayFilterFactory modifyResponseBodyFilterFactory;

    public GlobalFilter(ModifyResponseBodyGatewayFilterFactory modifyResponseBodyFilterFactory)
    {
        super(Config.class);
        this.modifyResponseBodyFilterFactory = modifyResponseBodyFilterFactory;
    }

    @Autowired
    private ApiTestController apiTestController;

    @Override
    public GatewayFilter apply(Config config) {
        final ModifyResponseBodyGatewayFilterFactory.Config modifyResponseBodyFilterFactoryConfig = new ModifyResponseBodyGatewayFilterFactory.Config();
        modifyResponseBodyFilterFactoryConfig.setNewContentType(MediaType.TEXT_HTML_VALUE);
        // modifyResponseBodyFilterFactoryConfig.setRewriteFunction(String.class, String.class, (exchange, bodyAsString) -> Mono.just(""));

        modifyResponseBodyFilterFactoryConfig.setRewriteFunction(String.class, String.class, (exchange, bodyAsString) -> {

            final String output = "";

            ServerHttpRequest request = exchange.getRequest();
            log.debug("GlobalFilter Request : [{}] " + request.getURI().toString());
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.GATEWAY_TIMEOUT);                 // HTTP Status Code 변경 예제
            response.getHeaders().remove("Content-Type");                   // 헤더 제거 예제
            return Mono.just(bodyAsString);                                     // 바디 변경 예제
        });

        return modifyResponseBodyFilterFactory.apply(modifyResponseBodyFilterFactoryConfig);
    }

    @Data
    public static class Config {
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}