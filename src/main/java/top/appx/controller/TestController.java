package top.appx.controller;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class TestController {
    @RequestMapping("/test")
    public String tt(ModelMap modelMap, HttpServletRequest request){
        Map<String,String[]> map = request.getParameterMap();

        for (String s : map.keySet()) {
            System.out.println(s+"===="+map.get(s));
        }
        System.out.println("invoke test");
        return "test";
    }
}
