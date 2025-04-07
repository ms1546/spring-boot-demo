package com.example.demo.controller;

import com.example.demo.model.FortuneResponse;
import com.example.demo.service.FortuneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fortune")
public class FortuneRestController {

    private final FortuneService fortuneService;

    public FortuneRestController(FortuneService fortuneService) {
        this.fortuneService = fortuneService;
    }

    /**
     * ランダム運勢の取得
     * GET /api/fortune?name=...
     */
    @GetMapping
    public ResponseEntity<FortuneResponse> getRandomFortune(
            @RequestParam(name = "name", required = false, defaultValue = "") String name
    ) {
        String fortuneType = fortuneService.getRandomFortune();
        FortuneResponse response = buildFortuneResponse(name, fortuneType);
        // return new ResponseEntity<>(response, HttpStatus.OK) と等価
        return ResponseEntity.ok(response);
    }

    /**
     * 今日の運勢の取得
     * GET /api/fortune/today?name=...
     */
    @GetMapping("/today")
    public ResponseEntity<FortuneResponse> getTodayFortune(
            @RequestParam(name = "name", required = false, defaultValue = "") String name
    ) {
        String fortuneType = fortuneService.getTodayFortune(name);
        FortuneResponse response = buildFortuneResponse(name, fortuneType);
        return ResponseEntity.ok(response);
    }

    /**
     * キャッシュクリア処理
     * POST /api/fortune/clear
     */
    @PostMapping("/clear")
    public ResponseEntity<String> clearCache() {
        fortuneService.clearCache();
        return ResponseEntity.ok("Cleared all fortune cache.");
    }

    /**
     * FortuneResponse の生成用ヘルパーメソッド
     */
    private FortuneResponse buildFortuneResponse(String name, String fortuneType) {
        FortuneResponse response = new FortuneResponse();
        response.setName(name);
        response.setResult(fortuneType);
        return response;
    }
}
