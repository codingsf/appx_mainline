package top.appx.ws;

import com.alibaba.fastjson.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import top.appx.config.ApplicationContextStatic;
import top.appx.entity.Chat_User;
import top.appx.entity.User;
import top.appx.service.Chat_UserService;
import top.appx.service.UserService;
import top.appx.util.ResponseMap;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@ServerEndpoint("/chat")
//@Component
public class ChatWS {
    private static Map<Long, ChatWS> wsMap = new HashMap<>();
    private Session session;
    private User user;
    private UserService userService;
    private Chat_UserService chat_userService;
    private static Long guest = -1L;


    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        System.out.println("opend");
        ApplicationContext applicationContext = ApplicationContextStatic.applicationContext;
        if(applicationContext!=null){
            userService = applicationContext.getBean(UserService.class);
            chat_userService = applicationContext.getBean(Chat_UserService.class);
        }
        sendOnlinesInfo();
    }
    private void  sendOnlinesInfo(){
        List<Object> list = new ArrayList<>();
        wsMap.forEach((key,value)->{
            if(this.user!=null && this.user.getId()==key){
                return;
            }
            User user = value.user;
            list.add(ResponseMap.instance()
                    .p("username",user.getNickname())
                    .p("id",user.getId())
                    .p("avatar",user.getAvatar())
            );
        });

        Object data = ResponseMap.instance().p("list",list);

        try {
            sendData(data, "onlinesInfo");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @OnMessage
    public void onMsg(String msg, Session session)throws IOException {
        ReqMsg reqMsg = JSONObject.parseObject(msg,ReqMsg.class);
        Map<String,String> data = reqMsg.getData();
        switch (reqMsg.getType()){
            case "init"://初始化
                String icard = data.get("icard");
                if(icard==null){
                    //游客
                    User guestUser = new User();
                    guestUser.setNickname("游客"+Math.abs(guest--));
                    guestUser.setAvatar("http://tva1.sinaimg.cn/crop.219.144.555.555.180/0068iARejw8esk724mra6j30rs0rstap.jpg");
                    guestUser.setId(guest);
                    this.user = guestUser;
                }else{
                    User user = userService.findByIcard(icard);
                    if(user==null){
                        throw new RuntimeException("认证失败");
                    }
                    this.user = user;
                }

                this.sendData(ResponseMap.instance()
                                .p("nickname",this.user.getNickname())
                            .p("id",this.user.getId())
                        .p("avatar",this.user.getAvatar()),
                        "recInit"
                );


                wsMap.forEach((key, chatWS)->{
                    Object data0 = ResponseMap.instance().p("id",this.user.getId())
                            .p("username",this.user.getNickname())
                            .p("avatar",this.user.getAvatar())
                            ;
                    try {
                        chatWS.sendData(data0,"userOnline");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                wsMap.put(this.user.getId(),this);
                break;
            case "chat"://聊天内容
                String content = data.get("content");
                String type = data.get("type");
                Long target = Long.parseLong(data.get("target"));

                Chat_User chat_user = new Chat_User();
                chat_user.setContent(content);
                chat_user.setType(type);
                chat_user.setTarget(target);
                chat_user.setUserId(this.user.getId());
                chat_user.setUserNickname(this.user.getNickname());
                chat_user.setAvatar(this.user.getAvatar());

                if("friend".equals(type)){
                    ChatWS chatWS = wsMap.get(target);
                    if(chatWS !=null){
                        chatWS.sendData(chat_user,"chat");
                    }
                }
                else if("group".equals(type)){
                    wsMap.forEach((key, chatWS)->{
                        if(key==this.user.getId()){
                            return;
                        }
                        try {
                            chatWS.sendData(chat_user,"chat");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }


                chat_userService.insert(chat_user);

                break;
        }

    }

    private void sendData(Object data,String type)throws IOException{
        session.getBasicRemote().sendText(JSONObject.toJSONString(ResponseMap.instance().p("type",type).p("data",data)));
    }
    private void sendMsg(Object obj)throws IOException{
        session.getBasicRemote().sendText(JSONObject.toJSONString(obj));
    }

    @OnClose
    public void onClose(){
        if(this.user!=null) {
            wsMap.remove(this.user.getId());
            wsMap.forEach((key, chatWS)->{
                Object data = ResponseMap.instance().p("id",this.user.getId());
                try {
                    chatWS.sendData(data,"userLeave");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

}
