package com.example.demo.controller;

import com.example.demo.service.FortuneBroadcastService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/fortune")
public class FortuneSseController {

    private final FortuneBroadcastService broadcastService;

    public FortuneSseController(FortuneBroadcastService broadcastService) {
        this.broadcastService = broadcastService;
    }

    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream() {
        return broadcastService.subscribeSse();
    }
}
