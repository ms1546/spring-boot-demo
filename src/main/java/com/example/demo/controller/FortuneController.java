package com.example.demo.controller;

import com.example.demo.service.FortuneService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 運勢（Fortune）を表示するコントローラクラス。
 * Thymeleafなどのテンプレートと連携してHTMLを返します。
 */
@Controller
@RequestMapping("/fortune")
public class FortuneController {

    private final FortuneService fortuneService;

    public FortuneController(FortuneService fortuneService) {
        this.fortuneService = fortuneService;
    }

    /**
     * ランダムな運勢ページを表示します。
     * 例: GET /fortune?name=User
     *
     * @param name ユーザ名（任意）
     * @param model Thymeleaf等のViewテンプレートで使用するModel
     * @return テンプレート名
     */
    @GetMapping
    public String showRandomFortunePage(
            @RequestParam(name = "name", defaultValue = "") String name,
            Model model
    ) {
        String fortuneType = fortuneService.getRandomFortune();
        return buildFortuneView(fortuneType, name, model);
    }

    /**
     * 今日の運勢ページを表示します。
     * 例: GET /fortune/today?name=User
     *
     * @param name ユーザ名（任意）
     * @param model Thymeleaf等のViewテンプレートで使用するModel
     * @return テンプレート名
     */
    @GetMapping("/today")
    public String showTodayFortunePage(
            @RequestParam(name = "name", defaultValue = "") String name,
            Model model
    ) {
        String fortuneType = fortuneService.getTodayFortune(name);
        return buildFortuneView(fortuneType, name, model);
    }

    /**
     * fortuneType と name をモデルに設定し、対応するテンプレート(HTML)を返す。
     *
     * @param fortuneType 運勢の種類（"goodFortune" など）
     * @param name        ユーザ名
     * @param model       Viewに渡すModel
     * @return テンプレート名 (例: "goodFortune.html")
     */
    private String buildFortuneView(String fortuneType, String name, Model model) {
        model.addAttribute("name", name);
        // 運勢の種類をさらに利用したい場合は model.addAttribute("fortuneType", fortuneType); なども可能
        return fortuneType + ".html";
    }
}
