package com.stromae.userservice.feign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
public class VideoServiceClientFallbackFactory implements FallbackFactory<VideoServiceClient> {

    private static final Logger log = LoggerFactory.getLogger(VideoServiceClientFallbackFactory.class);

    @Override
    public VideoServiceClient create(Throwable cause) {
        return new VideoServiceClient() {
            @Override
            public Map<String, Object> getVideoById(Long id) {
                log.error("Fallback triggered for getVideoById({}): {}", id, cause.getMessage());
                return Collections.singletonMap("error", "video-service unavailable");
            }
        };
    }
}
