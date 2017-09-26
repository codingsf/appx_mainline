package top.appx.controller.api;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.appx.entity.Chat_Group;
import top.appx.entity.Chat_User;
import top.appx.entity.User;
import top.appx.service.UserService;
import top.appx.service.Chat_GroupService;
import top.appx.service.Chat_UserService;
import top.appx.zutil.ResponseMap;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/chat_user")
public class Api_Chat_UserController {

    @Autowired
    private Chat_UserService chat_userService;

    @Autowired
    private UserService userService;


    @Autowired
    private Chat_GroupService chat_groupService;


    @PostMapping
    public void insert(Chat_User chat_user){
        chat_userService.insert(chat_user);
    }

    @GetMapping("/getInitList")
    public Object getInitList(){

        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        Object mine = null;
        if(user==null){
            mine = ResponseMap.instance().p("username","游客")
                    .p("id",0)
                    .p("status","online")
                    .p("sign","我就是我,不一样的我")
                    .p("avatar","http://tva1.sinaimg.cn/crop.219.144.555.555.180/0068iARejw8esk724mra6j30rs0rstap.jpg");

        }else{
            mine = ResponseMap.instance().p("username",user.getNickname())
                    .p("id",user.getId())
                    .p("status","online")
                    .p("sign","我就是我,不一样的我")
                    .p("avatar",user.getAvatar());
        }




   /*     Object list1 = new Object[]{
            ResponseMap.instance().p("username","超级管理员")
                .p("id",1)
                .p("avatar","http://tp1.sinaimg.cn/1571889140/180/40030060651/1")
                .p("sign","管理员admin"),
                ResponseMap.instance().p("username","zwy")
                        .p("id",2)
                        .p("avatar","http://tp3.sinaimg.cn/1223762662/180/5741707953/0")
                        .p("sign","我是zwy")
        };*/

        List<Object> list1 = new ArrayList<>();
        List<User> userList =userService.findManager();
        userList.forEach(user1 -> {
            list1.add( ResponseMap.instance().p("username",user1.getNickname())
                    .p("id",user1.getId())
                    .p("avatar",user1.getAvatar())
                    );
        });



        Object friend = new Object[]{
            ResponseMap.instance().p("groupname","管理员")
                    .p("id",1)
                    .p("online",2)
                .p("list",list1),
                ResponseMap.instance().p("groupname","在线用户")
                .p("id",0)
                .p("online",3)
        };


        List<Object> group = new ArrayList<>();
        List<Chat_Group> cgList = chat_groupService.find();
        cgList.forEach(chat_group -> {
            group.add(ResponseMap.instance().p("groupname",chat_group.getName())
                    .p("id",chat_group.getId())
                    .p("avatar",chat_group.getAvatar())
            );
        });

        Object data = ResponseMap.instance().p("mine",mine)
                .p("friend",friend)
                .p("group",group);

        return ResponseMap.instance().p("code",0).p("msg","")
                .p("data",data);
    }
}
