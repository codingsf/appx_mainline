package top.appx.controller.api;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.appx.controller.BaseController;
import top.appx.entity.CollectParam;
import top.appx.service.CollectParamService;

import java.util.List;

@RestController
@RequestMapping("/api/collectParam")
public class Api_CollectParamController extends BaseController {

    @Autowired
    private CollectParamService collectParamService;

    @RequiresPermissions("collectparam:manager")
    @GetMapping
    public Object list(
            @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize",defaultValue = "20") int pageSize)throws Exception{
        return collectParamService.findPage(pageNum,pageSize);
    }

    @RequiresPermissions("collectparam:add")
    @PostMapping
    public void save(CollectParam collectParam){
        collectParamService.save(collectParam);
    }


    @RequiresPermissions("collectparam:modify")
    @PutMapping
    public void update(CollectParam collectParam){
        collectParamService.update(collectParam);
    }

    @RequiresPermissions("collectparam:del")
    @DeleteMapping("/{ids}")
    public void del(@PathVariable("ids") List<Long> ids){
        collectParamService.deleteByIds(ids);
    }


}
