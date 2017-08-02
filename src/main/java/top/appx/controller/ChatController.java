package top.appx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;

@Controller
public class ChatController {
    @GetMapping("/chat")
    public String index(ModelMap modelMap){
        System.out.println("chat");
        return "/chat/index";
    }
    @GetMapping("/chat/mobile")
    public String index2(ModelMap modelMap){
        System.out.println("chat");
        return "/chat/mobile";
    }
}
