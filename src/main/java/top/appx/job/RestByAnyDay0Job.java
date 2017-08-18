package top.appx.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.appx.service.UserService;

@Component
public class RestByAnyDay0Job {
    @Autowired
    private UserService userService;

    public void execute(){
        userService.resetByAnyDay0();
    }
}
