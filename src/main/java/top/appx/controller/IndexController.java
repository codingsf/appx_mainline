package top.appx.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import top.appx.entity.User;
import top.appx.entity.vo.ArticleIndexVO;
import top.appx.service.ArticleService;
import top.appx.service.UserService;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/")
public class IndexController extends BaseController {
    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @GetMapping
    public String index(ModelMap modelMap){
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        if(user!=null) {
            user = userService.findById(user.getId());
        }
        modelMap.put("user",user);

        List<ArticleIndexVO> articleList = articleService.index();
        modelMap.put("articleList",articleList);


        return "/index";
    }

    @GetMapping("/sign")
    public String sign(){
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        if(user!=null) {
            user = userService.findById(user.getId());
            if(user.getSignTime()==null){
                userService.sign(user.getId());
            }
        }
        return "redirect:/";
    }

    @GetMapping("/invite/{userId}")
    public String yaoqing(HttpSession session,@PathVariable("userId") Long userId){
        session.setAttribute("inviteUserId",userId);
        logger.info("邀请链接有人进入:"+userId);
        return "redirect:/";
    }

    @RequestMapping("/frame")
    public String frame(){
        return "/frame";
    }

    @RequestMapping("/help")
    public String help(){
        return "/help";
    }




}
