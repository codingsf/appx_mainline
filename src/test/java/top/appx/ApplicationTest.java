package top.appx;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import top.appx.entity.Notify;
import top.appx.entity.User;
import top.appx.job.BtcOrderJob_jubi;
import top.appx.service.ArticleService;
import top.appx.service.BtcOrderService;
import top.appx.service.MailService;
import top.appx.service.NotifyService;

public class ApplicationTest extends BaseTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private BtcOrderService btcOrderService;

    @Autowired
    private BtcOrderJob_jubi btcOrderJob_jubi;


    @Test
    public void test()throws Exception{
        btcOrderJob_jubi.execute();
    }

}
