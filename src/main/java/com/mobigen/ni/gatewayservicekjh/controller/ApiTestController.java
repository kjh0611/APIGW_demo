package com.mobigen.ni.gatewayservicekjh.controller;

import com.mobigen.ni.gatewayservicekjh.config.WebClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class ApiTestController {

    @Autowired
    private TestFeign testFeign;

    @Autowired
    private WebClientConfig webClient;

    @GetMapping("/webClientTest")
    public Mono<String> doTest() {
        return webClient.webClient().get()
            .uri("/nms/api/v1/fm/alarm/filter/grade")
            .retrieve()
            .bodyToMono(String.class);
    }
}
