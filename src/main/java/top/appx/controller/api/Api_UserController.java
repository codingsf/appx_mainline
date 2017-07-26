package top.appx.controller.api;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.appx.controller.BaseController;
import top.appx.entity.User;
import top.appx.exception.UsernameExistException;
import top.appx.service.UserService;

import javax.servlet.http.HttpServletRequest;

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

}
