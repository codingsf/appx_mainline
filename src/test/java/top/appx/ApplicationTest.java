package top.appx;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import top.appx.entity.Notify;
import top.appx.service.MailService;
import top.appx.service.NotifyService;

public class ApplicationTest extends BaseTest {
    @Autowired
    private MailService mailService;
    @Autowired
    private NotifyService notifyService;
    @Test
    public void test1()throws Exception{
        System.out.println("开始发送邮件");

        Notify notify = new Notify();
        notify.setType("email");
        notify.setTarget("799378666@qq.com");
        notify.setTitle("ttttaaa");
        notify.setContent("tttt");
        notifyService.save(notify);
        System.out.println("执行结束");
    }
}
