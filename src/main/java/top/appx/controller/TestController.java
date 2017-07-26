package top.appx.controller;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/test")
public class TestController {
    @RequiresAuthentication
    @GetMapping
    public String tt(ModelMap modelMap){
        System.out.println("tt method invoke");
        modelMap.put("aa","bb");
        List<String> list = new ArrayList<>();
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");
        modelMap.put("strList",list);
        return "/test/index";
    }
}
