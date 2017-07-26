package top.appx.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.appx.entity.Article;
import top.appx.entity.Notify;
import top.appx.entity.User;
import top.appx.service.NotifyService;
import top.appx.service.UserService;
import top.appx.util.StringUtil;

import java.util.List;

@Aspect
@Component
public class NotifyAspect {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private UserService userService;

    private void notice(Article article){
        logger.info("新增文章 === " + article.getTitle() + " ,flag=" + article.getArticleGroupFlag() + ",grouId=" + article.getArticleGroupId());
        List<User> userList = userService.findSubscribeUser(article.getArticleGroupId());
        for (int i = 0; i < userList.size(); i++) {
            try {
                User user = userList.get(i);
                Notify notify = new Notify();
                notify.setTarget(user.getEmail());
                notify.setType("email");
                notify.setTitle(article.getTitle());
                String url = "http://news.appx.top/articles/"+article.getId();
                String content = "详情:<a href='"+url+"'>"+url+"</a>";
                notify.setContent(content);
                notifyService.save(notify);
            }catch (Exception ex){
                logger.error("发送通知出错==",ex);
            }
        }
    }
    @After(value = "execution(public * top.appx.service.impl.ArticleServiceImpl.save(..))")
    public void t(JoinPoint joinPoint){
        try {
            Article article = (Article) joinPoint.getArgs()[0];
            notice(article);
        }catch (Exception ex){
            logger.error("通知出错",ex);
        }

    }

    @AfterReturning(value = "execution(public * top.appx.service.impl.ArticleServiceImpl.saveIfNotExistUrl(..))",returning = "retValue")
    public void t2(JoinPoint joinPoint,boolean retValue){
        try {
            if(retValue) {
                Article article = (Article) joinPoint.getArgs()[0];
                notice(article);
            }
        }catch (Exception ex){
            logger.error("通知出错",ex);
        }

    }



}
