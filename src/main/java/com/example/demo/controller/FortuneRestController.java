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

    /** キャッシュクリア時の固定メッセージ */
    private static final String CLEAR_CACHE_MESSAGE = "Cleared all fortune cache.";

    private final FortuneService fortuneService;

    public FortuneRestController(FortuneService fortuneService) {
        this.fortuneService = fortuneService;
    }

    /** ランダム運勢の取得  GET /api/fortune?name=... */
    @GetMapping
    public ResponseEntity<FortuneResponse> getRandomFortune(
            @RequestParam(name = "name", required = false, defaultValue = "") String name) {

        String fortuneType = fortuneService.getRandomFortune();
        return ResponseEntity.ok(buildFortuneResponse(name, fortuneType));
    }

    /** 今日の運勢の取得  GET /api/fortune/today?name=... */
    @GetMapping("/today")
    public ResponseEntity<FortuneResponse> getTodayFortune(
            @RequestParam(name = "name", required = false, defaultValue = "") String name) {

        String fortuneType = fortuneService.getTodayFortune(name);
        return ResponseEntity.ok(buildFortuneResponse(name, fortuneType));
    }

    /** キャッシュクリア  POST /api/fortune/clear */
    @PostMapping("/clear")
    public ResponseEntity<String> clearCache() {
        fortuneService.clearCache();
        return ResponseEntity.ok(CLEAR_CACHE_MESSAGE);
    }

    /** FortuneResponse の生成ヘルパー（インスタンス状態を使わないため static 可） */
    private static FortuneResponse buildFortuneResponse(String name, String fortuneType) {
        FortuneResponse response = new FortuneResponse();
        response.setName(name);
        response.setResult(fortuneType);
        return response;
    }
}
