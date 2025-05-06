package com.example.demo.controller;

import com.example.demo.model.FortuneResponse;
import com.example.demo.service.FortuneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Fortune REST API
 */
@RestController
@RequestMapping("/api/fortune")
public class FortuneRestController {

    private static final String CLEAR_CACHE_MESSAGE = "Cleared all fortune cache.";

    private final FortuneService fortuneService;

    public FortuneRestController(FortuneService fortuneService) {
        this.fortuneService = fortuneService;
    }

    @GetMapping
    public ResponseEntity<FortuneResponse> getRandomFortune(
            @RequestParam(name = "name", required = false, defaultValue = "") String name) {

        String fortuneType = fortuneService.getRandomFortune();
        return ResponseEntity.ok(buildFortuneResponse(name, fortuneType));
    }

    @GetMapping("/today")
    public ResponseEntity<FortuneResponse> getTodayFortune(
            @RequestParam(name = "name", required = false, defaultValue = "") String name) {

        String fortuneType = fortuneService.getTodayFortune(name);
        return ResponseEntity.ok(buildFortuneResponse(name, fortuneType));
    }

    @PostMapping("/clear")
    public ResponseEntity<String> clearCache() {
        fortuneService.clearCache();
        return ResponseEntity.ok(CLEAR_CACHE_MESSAGE);
    }

    private static FortuneResponse buildFortuneResponse(String name, String fortuneType) {
        FortuneResponse response = new FortuneResponse();
        response.setName(name);
        response.setResult(fortuneType);
        return response;
    }
}
