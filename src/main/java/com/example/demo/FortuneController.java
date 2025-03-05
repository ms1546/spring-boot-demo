package main.java.com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import com.example.demo.model.FortuneResponse;

@Controller
public class FortuneController {

    @Autowired
    private FortuneService fortuneService;

    @RequestMapping("/fortune")
    public String start(
            @RequestParam(name = "name", required = false) String name,
            Model model
    ) {
        String fortuneType = fortuneService.getFortune();

        model.addAttribute("name", name);

        return fortuneType + ".html";
    }

    /**
     * JSONを返却するエンドポイント
     * 例: ブラウザやJavaScriptから "/api/fortune" にアクセス
     */
    @GetMapping("/api/fortune")
    @ResponseBody
    public FortuneResponse getFortuneAsJson(
            @RequestParam(name = "name", required = false) String name
    ) {
        // サービスからフォーチュンの種類を取得
        String fortuneType = fortuneService.getFortune();

        // 結果をJSON形式で返すため、FortuneResponse に詰める
        FortuneResponse response = new FortuneResponse();
        response.setName(name);
        response.setResult(fortuneType);  // "greatFortune" など

        return response;
    }
}
