package top.appx.controller.api;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.appx.entity.Config;
import top.appx.service.ConfigService;

import java.util.List;

@RestController
@RequestMapping("/api/configs")
public class Api_ConfigController {
    @Autowired
    private ConfigService configService;

    @RequiresPermissions("config:manager")
    @GetMapping
    public Object list(
            @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize",defaultValue = "20") int pageSize)throws Exception{
        return configService.findPage(pageNum,pageSize);
    }
    @RequiresPermissions("config:add")
    @PostMapping
    public void add(Config config){
        configService.save(config);
    }
    @RequiresPermissions("config:modify")
    @PutMapping
    public void update(Config config){
        configService.update(config);
    }

    @RequiresPermissions("config:del")
    @DeleteMapping("/{ids}")
    public void delete(@PathVariable("ids") List<Long> ids){
        configService.delete(ids);
    }



}
