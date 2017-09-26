package top.appx.controller.api;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.appx.controller.BaseController;
import top.appx.entity.User;
import top.appx.exception.UsernameExistException;
import top.appx.service.UserService;
import top.appx.zutil.ResponseMap;

import java.util.List;

/**
 * Created by qq799 on 2017/7/4.
 */
@RestController
@RequestMapping("/api/users")
public class Api_UserController extends BaseController {
    @Autowired
    private UserService userService;


    @RequiresPermissions("user:manager")
    @GetMapping
    public Object list(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",defaultValue = "20") int pageSize)throws Exception{
        return userService.findPage(pageNum,pageSize);
    }
    @RequiresPermissions("user:add")
    @PostMapping
    public void save(User user,Long roleId){
        if(roleId==null){
            roleId = 2L;
        }
        logger.debug("添加用户! user={}",user);
        try {
            userService.saveUserAndUserRole(user,roleId);
        }
        catch (UsernameExistException e0){

        }

        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequiresPermissions("user:modify")
    @PutMapping
    public void update(User user){
        userService.update(user);
    }


    @RequiresPermissions("user:del")
    @DeleteMapping("/{ids}")
    public void delete(@PathVariable("ids") List<Long> ids){
        userService.deleteByIds(ids);
    }


    @RequestMapping("/icard")
    public Object icard(){
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        String icard = userService.createIcard(user);
        return ResponseMap.instance().p("icard",icard);
    }

}
