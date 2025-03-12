package main.java.com.example.demo;

import com.example.demo.model.FortuneResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class FortuneController {

    @Autowired
    private FortuneService fortuneService;

    @RequestMapping("/fortune")
    public String start(
            @RequestParam(name = "name", required = false) String name,
            Model model
    ) {
        String fortuneType = fortuneService.getRandomFortune();
        model.addAttribute("name", name);
        return fortuneType + ".html";
    }

    @RequestMapping("/fortune/today")
    public String todayFortune(
            @RequestParam(name = "name", required = false) String name,
            Model model
    ) {
        String fortuneType = fortuneService.getTodayFortune(name);
        model.addAttribute("name", name);
        return fortuneType + ".html";
    }

    @GetMapping("/api/fortune")
    @ResponseBody
    public FortuneResponse getFortuneAsJson(
            @RequestParam(name = "name", required = false) String name
    ) {
        String fortuneType = fortuneService.getRandomFortune();
        FortuneResponse response = new FortuneResponse();
        response.setName(name);
        response.setResult(fortuneType);
        return response;
    }

    @GetMapping("/api/fortune/today")
    @ResponseBody
    public FortuneResponse getTodayFortuneAsJson(
            @RequestParam(name = "name", required = false) String name
    ) {
        String fortuneType = fortuneService.getTodayFortune(name);
        FortuneResponse response = new FortuneResponse();
        response.setName(name);
        response.setResult(fortuneType);
        return response;
    }

    @PostMapping("/api/fortune/clear")
    @ResponseBody
    public String clearCache() {
        fortuneService.clearCache();
        return "Cleared all fortune cache.";
    }
}
