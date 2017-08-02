package top.appx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@RequestMapping("/")
public class IndexController {
    @GetMapping
    public String index(ModelMap modelMap){
        modelMap.put("now",new Date());
        return "/index";
    }

    @RequestMapping("/frame")
    public String frame(){
        return "/frame";
    }

    @RequestMapping("/footer")
    public String footer(){
        return "/footer";
    }


}
