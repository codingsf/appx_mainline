package top.appx.controller;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.appx.config.AppxConfig;
import top.appx.entity.Article;
import top.appx.entity.vo.ArticleDetailVO;
import top.appx.entity.vo.ArticleIndexVO;
import top.appx.exception.NotFoundMsgException;
import top.appx.service.ArticleService;

import java.util.List;

@Controller
@RequestMapping("/articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private AppxConfig appxConfig;

    @GetMapping
    public String list(ModelMap modelMap){
        List<ArticleIndexVO> articleList = articleService.index();
        modelMap.put("articleList",articleList);
        return "/articles/list";
    }

    @GetMapping("/add")
    public String add(){
        return "/articles/add";
    }
    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, ModelMap modelMap){
        System.out.println("ttttttttt");
        ArticleDetailVO article = articleService.detail(id);
        if(article==null){
            throw new NotFoundMsgException();
        }
        modelMap.put("entity",article);
        modelMap.put("domain",appxConfig.getDomain());

        return "/articles/detail";
    }






}
