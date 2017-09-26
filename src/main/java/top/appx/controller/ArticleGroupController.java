package top.appx.controller;

import com.github.pagehelper.PageInfo;
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
import top.appx.entity.vo.ArticleGroupVO;
import top.appx.exception.NotFoundMsgException;
import top.appx.service.ArticleGroupService;
import top.appx.service.ArticleService;

import java.util.List;

@Controller
@RequestMapping("/articleGroups")
public class ArticleGroupController {

    @Autowired
    private ArticleGroupService articleGroupService;

    @Autowired
    private ArticleService articleService;
    @GetMapping
    public String list(ModelMap modelMap){
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        Long userId = null;
        if(user!=null){
            userId = user.getId();
        }

        List<ArticleGroupVO> list = articleGroupService.index(userId);
        modelMap.put("articleGroupList",list);
        System.out.println("len "+list.size());
        return "/articleGroups/list";
    }



    @GetMapping("/{id}")
    public String detail(
            @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize",defaultValue = "20") int pageSize,
            @PathVariable("id") Long id, ModelMap modelMap)throws Exception{
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        Long userId = null;
        if(user!=null){
            userId = user.getId();
        }


        ArticleGroupVO articleGroup = articleGroupService.selectVO(id,userId);
        if(articleGroup==null){
            throw new NotFoundMsgException();
        }
        modelMap.put("entity",articleGroup);

        Article article = new Article();
        article.setArticleGroupId(articleGroup.getId());
        PageInfo<Article> pageInfo = articleService.findPage(article,pageNum,pageSize);
       // List<Article> articleList = articleService.findByArticleGroupId(articleGroup.getId());

        modelMap.put("pageInfo",pageInfo);


        modelMap.put("subscribe",articleGroup.isSubscribe());
        return "/articleGroups/detail";
    }


}
