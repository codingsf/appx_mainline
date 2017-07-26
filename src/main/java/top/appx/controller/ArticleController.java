package top.appx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import top.appx.entity.Article;
import top.appx.exception.NotFoundMsgException;
import top.appx.service.ArticleService;

import javax.websocket.server.PathParam;
import java.util.List;

@Controller
@RequestMapping("/articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @GetMapping
    public String list(ModelMap modelMap){
        List<Article> articleList = articleService.index();
        modelMap.put("articleList",articleList);
        return "/articles/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, ModelMap modelMap){
        System.out.println("ttttttttt");
        Article article = articleService.findById(id);
        if(article==null){
            throw new NotFoundMsgException();
        }
        modelMap.put("entity",article);

        return "/articles/detail";
    }


}
