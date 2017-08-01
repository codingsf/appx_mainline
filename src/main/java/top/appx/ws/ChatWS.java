package top.appx.ws;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import top.appx.config.ApplicationContextStatic;
import top.appx.entity.Chat_User;
import top.appx.service.Chat_UserService;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ServerEndpoint("/chat")
@Component
public class ChatWS {

    public static int index = 0;

    private Chat_UserService chat_userService;
    private static int onLineCount = 0;
    private Session session;
    private String userNickname;
    private static String[] avatars = new String[]{
            "http://tp2.sinaimg.cn/2386568184/180/40050524279/0",
            "http://tp1.sinaimg.cn/1571889140/180/40030060651/1",
            "http://tva3.sinaimg.cn/crop.0.0.180.180.180/7f5f6861jw1e8qgp5bmzyj2050050aa8.jpg",
            "http://tva3.sinaimg.cn/crop.0.0.512.512.180/8693225ajw8f2rt20ptykj20e80e8weu.jpg",
            "http://tp2.sinaimg.cn/1833062053/180/5643591594/0",
            "http://tp2.sinaimg.cn/1783286485/180/5677568891/1",
            "http://tp4.sinaimg.cn/2145291155/180/5601307179/1",
            "http://tp1.sinaimg.cn/1241679004/180/5743814375/0",
            "http://tva1.sinaimg.cn/crop.0.0.180.180.180/86b15b6cjw1e8qgp5bmzyj2050050aa8.jpg",
            "http://tp1.sinaimg.cn/5286730964/50/5745125631/0",
            "http://tp4.sinaimg.cn/1665074831/180/5617130952/0",
            "http://tp2.sinaimg.cn/2518326245/180/5636099025/0",
            "http://tp3.sinaimg.cn/1223762662/180/5741707953/0",
            "http://tp4.sinaimg.cn/1345566427/180/5730976522/0"

    };

    public static List<ChatWS> list =new ArrayList<>();
    public ChatWS(){
        System.out.println("new instance");
    }

    @OnOpen
    public void onOpen(Session session){
        userNickname ="用户"+index++;

        this.session = session;
        onLineCount++;
        System.out.println("onOpen");
        try {
            chat_userService = ApplicationContextStatic.applicationContext.getBean(Chat_UserService.class);
        }catch (Exception ex){
            //ex.printStackTrace();
        }
        list.add(this);
        System.out.println("List len:"+list.size());
    }

    @OnMessage
    public void onMsg(String msg, Session session)throws IOException{
        System.out.println("rec msg :"+msg);
        RecMsg recMsg = JSONObject.parseObject(msg,RecMsg.class);
        Chat_User chat_user = new Chat_User();
        chat_user.setUserId((long)index);
        chat_user.setTarget(recMsg.getTarget());
        chat_user.setContent(recMsg.getContent());
        chat_user.setType(recMsg.getType());
        try {
            if(chat_userService!=null){
                chat_userService.insert(chat_user);
            }
        }catch (Exception ex){
            //ex.printStackTrace();
        }

        if("group".equals(recMsg.getType())){

            list.forEach(chatWS -> {
                if(chatWS ==this){
                    return;
                }

                Chat_User chat_user1 =new Chat_User();
                chat_user1.setUserId(recMsg.getTarget());
                chat_user1.setType(recMsg.getType());
                chat_user1.setContent(recMsg.getContent());
                chat_user1.setAvatar(avatars[index%avatars.length]);
                chat_user1.setUserNickname(userNickname);
                try {
                    chatWS.sendMsg(chat_user1);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            });

        }
        else{
            Chat_User chat_user1 = new Chat_User();
            chat_user1.setType(recMsg.getType());
            chat_user1.setUserNickname("系统");
            chat_user1.setAvatar("http://qzapp.qlogo.cn/qzapp/100280987/56ADC83E78CEC046F8DF2C5D0DD63CDE/100");
            chat_user1.setUserId(recMsg.getTarget());
            chat_user1.setContent("该聊天功能尚未实现,暂时只能支群聊!");
            sendMsg(chat_user1);
        }


     //   chat_user1.setTarget(1L);


    }
    private void sendMsg(Object obj)throws IOException{
        session.getBasicRemote().sendText(JSONObject.toJSONString(obj));
    }

    @OnClose
    public void onClose(){
        System.out.println("closed");
        onLineCount--;
        list.remove(this);
    }
    @OnError
    public void onError(Session session,Throwable error){
        System.out.println("1112222发生错误11222"+error.getMessage());
    }

}
