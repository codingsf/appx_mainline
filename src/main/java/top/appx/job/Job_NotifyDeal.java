package top.appx.job;

import com.alibaba.fastjson.JSONObject;
import groovy.transform.Synchronized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.appx.config.AppxConfig;
import top.appx.entity.Notify;
import top.appx.entity.User;
import top.appx.service.MailService;
import top.appx.service.NotifyService;
import top.appx.service.TransferService;
import top.appx.service.UserService;
import top.appx.util.HttpUtil;

import java.util.Date;
import java.util.List;

@Component
public class Job_NotifyDeal {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private AppxConfig appxConfig;
    @Autowired
    private MailService mailService;

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private TransferService transferService;

    @Autowired
    private UserService userService;

    public synchronized void  execute(){
       List<Notify> notifyList = notifyService.waitDeal();
       notifyList.forEach(notify -> {
               deal(notify);
                notify.setDealTime(new Date());
               notifyService.dealResult(notify);
       });
    }


    public void deal(Notify notify){
        try {
            if(notify.getTargetUserId()!=null && notify.getTargetUserId()!=1){
                User user = userService.findById(notify.getTargetUserId());
                if(user.getMoney()<=0){
                    throw new Exception("积分不足");
                }
            }
            switch (notify.getType()){
                case "qq":
                    String msg = notify.getContent();
                    String result = null;
                    if(notify.getTarget().startsWith("group:")){
                        result = HttpUtil.httpPost("http://"+appxConfig.getQqreboot()+"/send_group_msg", "group_id=" + notify.getTarget().substring("group:".length()) + "&message=" + msg);
                    }else{
                        result = HttpUtil.httpPost("http://"+appxConfig.getQqreboot()+"/send_private_msg", "user_id=" + notify.getTarget() + "&message=" + msg);
                    }
                    String status = JSONObject.parseObject(result).getString("status");
                    if(!"ok".equals(status)){
                        throw new Exception(result);
                    }
                    break;
                default:
                    String title = notify.getTitle();
                    mailService.sendHtmlMail(notify.getTarget(), title, notify.getContent());
            }
            notify.setStatus("success");

            if(notify.getTargetUserId()!=null && notify.getTargetUserId()!=1) {
                String title = "消息转账(";
                if("qq".equals(notify.getType())){
                    title += "QQ";
                }
                else if("email".equals(notify.getType())){
                    title += "邮箱";
                }
                else if("sms".equals(notify.getType())){
                    title += "短信";
                }
                title += ")";

                transferService.transfer(notify.getTargetUserId(), 1L, 1, title, null);
            }


        }
        catch (Exception ex){
            notify.setStatus("error");
            notify.setErrorMsg(ex.getMessage());
            logger.error("发送邮件失败",ex);
        }
    }
}
