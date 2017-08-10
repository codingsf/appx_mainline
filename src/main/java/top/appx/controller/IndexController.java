package top.appx.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import top.appx.entity.User;

import java.util.Date;

@Controller
@RequestMapping("/")
public class IndexController {
    @GetMapping
    public String index(ModelMap modelMap){
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        modelMap.put("user",user);
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
