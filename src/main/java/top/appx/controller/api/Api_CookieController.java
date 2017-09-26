package top.appx.controller.api;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.appx.entity.Cookie;
import top.appx.service.CookieService;
import top.appx.controller.BaseController;

import java.util.List;

@RestController
@RequestMapping("/api/cookie")
public class Api_CookieController extends BaseController {
    @Autowired
    private CookieService cookieStrService;

    @RequiresPermissions("cookie:manager")
    @GetMapping
    public Object list(
            @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize",defaultValue = "20") int pageSize)throws Exception{
        return cookieStrService.findPage(pageNum,pageSize);
    }

    @RequiresPermissions("cookie:add")
    @PostMapping
    public void save(Cookie cookieStr){
        cookieStrService.save(cookieStr);
    }
    @RequiresPermissions("cookie:modify")
    @PutMapping
    public void update(Cookie cookieStr){
        cookieStrService.update(cookieStr);
    }

    @RequiresPermissions("cookie:del")
    @DeleteMapping("/{ids}")
    public void del(@PathVariable("ids") List<Long> ids){
        cookieStrService.del(ids);
    }

}
