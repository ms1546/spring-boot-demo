package com.example.demo.service;

import com.example.demo.model.FortuneHistory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Fortune 生成をリアルタイム配信 (SSE & WebSocket)
 */
@Service
public class FortuneBroadcastService {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter subscribeSse() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout   (() -> emitters.remove(emitter));
        return emitter;
    }

    private final SimpMessagingTemplate messagingTemplate;

    public FortuneBroadcastService(SimpMessagingTemplate template) {
        this.messagingTemplate = template;
    }

    public void broadcast(FortuneHistory history) {
        emitters.removeIf(e -> sendSse(e, history));

        messagingTemplate.convertAndSend("/topic/fortune", history);
    }

    private boolean sendSse(SseEmitter emitter, FortuneHistory history) {
        try {
            emitter.send(SseEmitter.event()
                                   .name("fortune")
                                   .data(history));
            return false;
        } catch (IOException | IllegalStateException ex) {
            return true;
        }
    }
}
