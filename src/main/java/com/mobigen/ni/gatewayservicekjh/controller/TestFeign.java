package com.mobigen.ni.gatewayservicekjh.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "target", url = "http://192.168.187:11000")
public interface TestFeign {

    @GetMapping(value = "/nms/api/v1/fm/alarm/filter/station")
    Object getData();
}
