package top.appx.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.appx.controller.BaseController;
import top.appx.entity.Cookie;
import top.appx.service.CookieService;

import java.util.List;

@RestController
@RequestMapping("/api/cookie")
public class Api_CookieController extends BaseController {
    @Autowired
    private CookieService cookieStrService;

    //@RequiresPermissions("cookiestr:manager")
    @GetMapping
    public Object list(
            @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize",defaultValue = "20") int pageSize)throws Exception{
        return cookieStrService.findPage(pageNum,pageSize);
    }

    @PostMapping
    public void save(Cookie cookieStr){
        cookieStrService.save(cookieStr);
    }
    @PutMapping
    public void update(Cookie cookieStr){
        cookieStrService.update(cookieStr);
    }

    @DeleteMapping("/{ids}")
    public void del(@PathVariable("ids") List<Long> ids){
        cookieStrService.del(ids);
    }

    @RequestMapping("/test")
    public Object test(){
        return cookieStrService.findAndMrak("tt");
    }

}
