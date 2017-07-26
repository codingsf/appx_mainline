package top.appx.controller.api;

import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.appx.entity.ArticleGroup;
import top.appx.entity.User;
import top.appx.service.ArticleGroupService;

import java.util.List;

@RestController
@RequestMapping("/api/articleGroups")
public class Api_ArticleGroupController {

    @Autowired
    private ArticleGroupService articleGroupService;

    @RequiresPermissions("articleGroup:manager")
    @GetMapping
    public Object list(
            @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize",defaultValue = "20") int pageSize)throws Exception{
        return articleGroupService.findPage(pageNum,pageSize);
    }

    @RequiresPermissions("articleGroup:add")
    @PostMapping
    public void save(ArticleGroup articleGroup){
        articleGroupService.save(articleGroup);
    }

    @RequiresPermissions("articleGroup:del")
    @DeleteMapping("/{ids}")
    public void delete(@PathVariable("ids") List<Long> ids){
        articleGroupService.deleteByIds(ids);
    }


    @PostMapping("/subscribe/{id}")
    public void subscribe(@PathVariable("id")Long id, @Param("subscribe")boolean subscribe){
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        long userId = user.getId();

        articleGroupService.subscribe(id,userId,subscribe);
    }
}

