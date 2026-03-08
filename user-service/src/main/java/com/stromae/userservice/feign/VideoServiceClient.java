package com.stromae.userservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "video-service", fallbackFactory = VideoServiceClientFallbackFactory.class)
public interface VideoServiceClient {

    @GetMapping("/api/videos/{id}")
    Map<String, Object> getVideoById(@PathVariable Long id);

}
