package top.appx.controller;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class TestController {
    @RequestMapping("/t1")
    @Cacheable(value = "aa",key="aa")
    public String t1(){
        return "/t1";
    }

}
