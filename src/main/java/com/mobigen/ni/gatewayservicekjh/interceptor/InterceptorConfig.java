package com.mobigen.ni.gatewayservicekjh.interceptor;

import com.mobigen.ni.gatewayservicekjh.config.WebClientConfig;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
@Configuration
public class InterceptorConfig implements WebFilter {

    public InterceptorConfig() {
        ;
    }

    @Autowired
    private WebClient webClient;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        log.debug("InterceptConfig Request : [{}] " + request.getURI().toString());

        //webClient 테스트
        Mono<ClientResponse> clientResponse = webClient
                .get()
                .uri("/nms/api/v1/fm/alarm/filter/grade")
                .exchange();
        clientResponse.subscribe((response) -> {

            // here you can access headers and status code
            ClientResponse.Headers headers = response.headers();
            HttpStatus stausCode = response.statusCode();

            Mono<String> bodyToMono = response.bodyToMono(String.class);
            // the second subscribe to access the body
            bodyToMono.subscribe((body) -> {

                // here you can access the body
                log.debug("============== WebClient =============");
                log.debug("body:" + body);

                // and you can also access headers and status code if you need
                log.debug("headers:" + headers.asHttpHeaders());
                log.debug("stausCode:" + stausCode);

            }, (ex) -> {
                // handle error
            });
        });

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