package top.appx.controller.api;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.appx.controller.BaseController;
import top.appx.entity.Article;
import top.appx.service.ArticleService;
import top.appx.zutil.ResponseMap;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class Api_ArticleController extends BaseController {

    @Autowired
    private ArticleService articleService;


    @RequiresPermissions("article:manager")
    @GetMapping
    public Object list(
            @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize",defaultValue = "20") int pageSize,Article search)throws Exception{

        return articleService.findPage(search,pageNum,pageSize);
    }


    @GetMapping("/{id}")
    public Object findById(@PathVariable("id") Long id){
        Article article =  articleService.findById(id);
        return article;
    }

    @RequiresPermissions("article:del")
    @DeleteMapping("/{ids}")
    public void delete(@PathVariable("ids") List<Long> ids){
        articleService.deleteByIds(ids);
    }



    @RequiresPermissions("article:add")
    @PostMapping
    public Object save(Article article){
        logger.debug("文章:"+article.getTitle());
        articleService.save(article);
        return ResponseMap.instance().p("id",article.getId());
    }

//    @RequiresPermissions("article:modify")
    @PutMapping
    public void modify(Article article){
        articleService.update(article);
    }


}
