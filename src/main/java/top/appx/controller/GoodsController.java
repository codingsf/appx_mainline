package top.appx.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import top.appx.entity.Goods;
import top.appx.entity.User;
import top.appx.exception.MsgException;
import top.appx.service.GoodsService;
import top.appx.util.ResponseMap;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;
    @GetMapping("/sell/{id}")
    public String sell(@PathVariable("id") long id, ModelMap modelMap){
        Goods goods = goodsService.findById(id);
        modelMap.put("entity",goods);
        return "/goods/sell";
    }


    @PostMapping("/{id}")
    @ResponseBody
    public void buy(Goods goods){

        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();

        goodsService.buy(goods,user.getId());

    }

    @PutMapping("/{id}")
    @ResponseBody
    public void update(Goods goods){
        goodsService.update(goods);
    }

}
