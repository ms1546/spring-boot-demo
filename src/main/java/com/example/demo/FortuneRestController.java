package com.example.demo.controller;

import com.example.demo.model.FortuneResponse;
import com.example.demo.service.FortuneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fortune")
public class FortuneRestController {

    private final FortuneService fortuneService;

    public FortuneRestController(FortuneService fortuneService) {
        this.fortuneService = fortuneService;
    }

    @GetMapping
    public ResponseEntity<FortuneResponse> getRandomFortuneAsJson(
            @RequestParam(name = "name", required = false, defaultValue = "") String name
    ) {
        String fortuneType = fortuneService.getRandomFortune();

        FortuneResponse response = new FortuneResponse();
        response.setName(name);
        response.setResult(fortuneType);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/today")
    public ResponseEntity<FortuneResponse> getTodayFortuneAsJson(
            @RequestParam(name = "name", required = false, defaultValue = "") String name
    ) {
        String fortuneType = fortuneService.getTodayFortune(name);

        FortuneResponse response = new FortuneResponse();
        response.setName(name);
        response.setResult(fortuneType);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/clear")
    public ResponseEntity<String> clearCache() {
        fortuneService.clearCache();
        return new ResponseEntity<>("Cleared all fortune cache.", HttpStatus.OK);
    }
}
