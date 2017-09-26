package top.appx.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.appx.config.AppxConfig;
import top.appx.entity.Article;
import top.appx.entity.ArticleGroup;
import top.appx.entity.Notify;
import top.appx.entity.User;
import top.appx.exception.NotEnoughMoneyException;
import top.appx.service.ArticleGroupService;
import top.appx.service.NotifyService;
import top.appx.service.TransferService;
import top.appx.service.UserService;
import top.appx.zutil.StringUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Aspect
@Component
public class NotifyAspect {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleGroupService articleGroupService;

    @Autowired
    private TransferService transferService;


    @Autowired
    private AppxConfig appxConfig;
    private void notice(Article article){
        logger.info("新增文章 === " + article.getTitle() + " ,flag=" + article.getArticleGroupFlag() + ",grouId=" + article.getArticleGroupId());

        if(article.getOccTime()!=null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(article.getOccTime());
            if(article.getOccTime().getHours()==0 && article.getOccTime().getMinutes()==0 && article.getOccTime().getSeconds()==0){
                calendar.add(Calendar.HOUR,24);
            }
            else{
                calendar.add(Calendar.MINUTE,30);
            }
            if(calendar.getTime().before(new Date())){
                logger.info("消息超时"+article.getTitle());
                return;
            }
        }

        ArticleGroup articleGroup = articleGroupService.findById(article.getArticleGroupId());
        String articleName = "";;
        if(articleGroup!=null){
            articleName = "【"+articleGroup.getName()+"】 ";
        }

        List<User> userList = userService.findSubscribeUser(article.getArticleGroupId());
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            try {
                double money = user.getMoney();
                Notify notify = new Notify();
                notify.setTarget(user.getEmail());
                notify.setTargetUserId(user.getId());
                notify.setType("email");
                String title = article.getTitle();
                title = title.charAt(0)+"-"+title.substring(1);
                title =articleName+title+" @"+user.getNickname();
                notify.setTitle(title);
                money--;
                String url = "http://"+appxConfig.getDomain()+"/articles/"+article.getId();
                String content = user.getNickname()+" 您好,您订阅的新闻有新消息,详情:<a href='"+url+"'>"+url+"</a><br />" +
                        "\n本次通知积分:-1" +
                        "\n剩余积分:"+money;
                notify.setContent(content);
                if(user.getEmailNotify()!=null && user.getEmailNotify() && !StringUtil.isNullOrEmpty(user.getEmail())){
                        notifyService.save(notify);
                }

                if(user.getQqNotify()!=null && user.getQqNotify() && !StringUtil.isNullOrEmpty(user.getQq())){
                    money--;
                    notify.setId(null);
                    notify.setType("qq");
                    notify.setTarget(user.getQq());
                    notify.setContent(articleName+article.getTitle()+"" +
                                    "\n@"+user.getNickname()+
                            "\n本次积分:-1" +
                            "\n剩余积分:"+money+"" +
                            "\n------------------------------------" +
                            "\nby燎火新闻网" +
                            "\n详情:"+url);
                    notify.setTitle(notify.getContent());
                    notifyService.save(notify);
                }
            }
            catch (NotEnoughMoneyException e0){
                logger.info("没有足够的积分,@"+user.getNickname());
            }
            catch (Exception ex){
                logger.error("发送通知出错==",ex);

            }
        }

   /*     final String articleNameF =articleName;
        new Thread(new Runnable() {
            @Override
            public void run() {
                    if(!StringUtil.isNullOrEmpty(appxConfig.getQqgroupids())){
                            String[] strs = appxConfig.getQqgroupids().split(",");
                            for (String str : strs) {
                                for(int i=0;i<5;i++) {
                                    try {
                                        String msg = articleNameF + "有新文章:<" + article.getTitle() + ">,链接:http://"+appxConfig.getDomain()+"/articles/" + article.getId();
                                        HttpUtil.httpPost("http://"+appxConfig.getQqreboot()+"/send_group_msg", "group_id=" + str + "&message=" + msg);
                                        break;
                                    }catch (Exception ex){
                                        logger.error("发送qq消息出错"+ex.getMessage());
                                    }
                                }
                            }
                    }
            }
        }).start();
*/

    }
    @After(value = "execution(public * top.appx.service.impl.ArticleServiceImpl.save(..))")
    public void t(JoinPoint joinPoint){
        Article article = (Article) joinPoint.getArgs()[0];

        try {
            notice(article);
        }

        catch (Exception ex){
            logger.error("通知出错",ex);
        }
    }

    @AfterReturning(value = "execution(public * top.appx.service.impl.ArticleServiceImpl.saveIfNotExistUrl(..))",returning = "retValue")
    public void t2(JoinPoint joinPoint,boolean retValue){
        try {
            if(retValue) {
                System.out.println("捕获");
                Article article = (Article) joinPoint.getArgs()[0];
                notice(article);
            }
        }catch (Exception ex){
            logger.error("通知出错",ex);
        }

    }



}
