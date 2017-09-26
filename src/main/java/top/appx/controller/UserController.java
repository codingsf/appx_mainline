package top.appx.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import top.appx.entity.User;
import top.appx.service.UserService;
import top.appx.entity.Goods;
import top.appx.service.GoodsService;
import top.appx.service.TransferService;

@Controller
public class UserController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private TransferService transferService;

    @Autowired
    private GoodsService goodsService;

    @GetMapping("/user")
    public String index(ModelMap modelMap){

        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        user = userService.findById(user.getId());
        modelMap.put("entity",user);
        modelMap.put("inviteUsers",userService.findByInviteUserId(user.getId()));
        modelMap.put("transfers",transferService.findUserId(user.getId(),30L));
        Goods search = new Goods();
        search.setOwnerUserId(user.getId());
        modelMap.put("goodss",goodsService.findVO(search));
        modelMap.put("self",true);
        return "/user/index";
    }

    @GetMapping("/user/{id}")
    public String user(@PathVariable("id")Long userId,ModelMap modelMap){
        Subject subject = SecurityUtils.getSubject();
        User user0 = (User)subject.getPrincipal();
        if(user0!=null && user0.getId()==userId){
            return "redirect:/user";
        }

        User user = userService.findById(userId);
        modelMap.put("entity",user);
        modelMap.put("inviteUsers",userService.findByInviteUserId(user.getId()));
        modelMap.put("transfers",transferService.findUserId(user.getId(),30L));
        Goods search = new Goods();
        search.setOwnerUserId(user.getId());
        modelMap.put("goodss",goodsService.findVO(search));
        modelMap.put("self",false);
        return "/user/index";
    }

    @GetMapping("/user/modifyqq")
    public String modifyQq(ModelMap modelMap){
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        user = userService.findById(user.getId());
        modelMap.put("entity",user);
        return "/user/modifyQq";
    }
    @PostMapping("/user/modifyqq")
    @ResponseBody
    public void modifyqq(@RequestParam("qq") String qq){
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        user = userService.findById(user.getId());
        user.setQq(qq);
        userService.update(user);
    }



    @GetMapping("/user/szqqnotify")
    public String szqqnotify(@RequestParam("qqNotify") boolean qqNotify){
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        user = userService.findById(user.getId());
        user.setQqNotify(qqNotify);

        userService.updateNotifySz(user);
        return "redirect:/user";
    }

    @GetMapping("/user/szemailnotify")
    public String szemailnotify(@RequestParam("emailNotify") boolean emailNotify){
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        user = userService.findById(user.getId());
        user.setEmailNotify(emailNotify);
        userService.updateNotifySz(user);

        return "redirect:/user";
    }

}
