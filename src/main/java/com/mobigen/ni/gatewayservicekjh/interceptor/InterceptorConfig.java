package com.mobigen.ni.gatewayservicekjh.interceptor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
public class InterceptorConfig implements WebFilter {

    public InterceptorConfig() {
        ;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        log.debug("InterceptConfig Request : [{}] " + request.getURI().toString());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataBufferUtils.join(exchange.getRequest().getBody()).flatMap(dataBuffer -> {
            try {

                byteArrayOutputStream.write(dataBuffer.asInputStream().readAllBytes());
                //dataBuffer.retainedSlice(0,dataBuffer.readableByteCount());
                log.debug("InterceptConfig Request body : [{}] ",byteArrayOutputStream.toString());
            } catch (IOException e) {
                log.debug("couldn't extract body from request", e);
            }
            return chain.filter(exchange);
        });

        return chain.filter(exchange);
    }
}