package top.appx.controller.api;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.appx.controller.BaseController;
import top.appx.entity.Config;
import top.appx.entity.Notify;
import top.appx.service.NotifyService;

import java.util.List;

@RestController
@RequestMapping("/api/notify")
public class Api_NotifyController extends BaseController {
    @Autowired
    private NotifyService notifyService;

    @RequiresPermissions("notify:manager")
    @GetMapping
    public Object list(
            @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize",defaultValue = "20") int pageSize,Notify search)throws Exception{
        return notifyService.findPage(search,pageNum,pageSize);
    }

    @RequiresPermissions("notify:add")
    @PostMapping
    public void add(Notify notify){
        notifyService.save(notify);
    }


    @RequiresPermissions("notify:del")
    @DeleteMapping("/{ids}")
    public void delete(@PathVariable("ids") List<Long> ids){
        notifyService.delete(ids);
    }



}
