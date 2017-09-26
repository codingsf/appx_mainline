package top.appx.controller;

import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.appx.entity.Article;
import top.appx.entity.User;
import top.appx.entity.vo.ArticleIndexVO;
import top.appx.service.ArticleService;
import top.appx.service.UserService;
import top.appx.service.TransferService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/")
public class IndexController extends BaseController {
    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private TransferService transferService;

    @GetMapping
    public String index(ModelMap modelMap){
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        if(user!=null) {
            user = userService.findById(user.getId());
            modelMap.put("transfers",transferService.findUserId(user.getId(),3L));

        }
        modelMap.put("user",user);

        List<ArticleIndexVO> articleList = articleService.articles();
        modelMap.put("articleList",articleList);

        modelMap.put("maxMoneyUsers",userService.moneyTop10());
        return "/index";
    }

    @GetMapping("/sign")
    public String sign(){
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        if(user!=null) {
            user = userService.findById(user.getId());
            if(user != null && user.getSignTime()==null){
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

    @RequestMapping("/search")
    public String search(@Param("keyWord")String keyWord, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                         @RequestParam(value = "pageSize",defaultValue = "20") int pageSize, ModelMap modelMap)throws Exception{
        modelMap.put("keyWord",keyWord);
        Article search = new Article();
        search.setTitle(keyWord);
        Object page = articleService.findPageVO(search,pageNum,pageSize);
        modelMap.put("pageInfo",page);
        return "/articles/search";
    }

    @RequestMapping("/nav")
    public String nav(){
        return "/nav";
    }



}
