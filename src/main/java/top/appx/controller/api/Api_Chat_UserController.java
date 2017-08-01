package top.appx.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.appx.entity.Chat_User;
import top.appx.service.Chat_UserService;

@RestController
@RequestMapping("/api/chat_user")
public class Api_Chat_UserController {

    @Autowired
    private Chat_UserService chat_userService;
    @PostMapping
    public void insert(Chat_User chat_user){
        chat_userService.insert(chat_user);
    }
}
