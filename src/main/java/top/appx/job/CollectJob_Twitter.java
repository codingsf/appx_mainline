package top.appx.job;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import top.appx.config.AppxConfig;
import top.appx.entity.Article;
import top.appx.entity.CollectParam;
import top.appx.factory.ArticleFactory;
import top.appx.service.ArticleService;
import top.appx.util.DateUtil;
import top.appx.util.StringUtil;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Date;

public class CollectJob_Twitter extends CollectJob  {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    private AppxConfig appxConfig;
    public CollectJob_Twitter(CollectParam collectParam, ApplicationContext applicationContext){
        super(collectParam,applicationContext);
        if(applicationContext!=null){
            appxConfig = applicationContext.getBean(AppxConfig.class);
        }
    }

    public void execute()throws Exception{

        String listUrl = "https://twitter.com/"+collectParam.getTwitterName();
        Element doc = Jsoup.connect(listUrl)
                .proxy(appxConfig.getProxy_host(),appxConfig.getProxy_port())
                .get();
        Elements items = doc.select("li.stream-item");
        for (int i = 0; i < items.size(); i++) {
            if(i>=3){
                break;
            }
            Element item = items.get(i);
            String content = item.select(".tweet-text").html();
            String timeStr = item.select(".js-short-timestamp").get(0).attr("data-time-ms");
            Date date = new Date(Long.parseLong(timeStr));
            String itemId = item.attr("data-item-id");
            String url = listUrl+"/status/"+itemId;

            String title = collectParam.getName()+"("+DateUtil.dateToString(date)+")";

            if(articleService!=null) {
                Article article = ArticleFactory.createArticle(collectParam.getArticleGroupFlag(), title, content, url, date);
                articleService.saveIfNotExistUrl(article);
            }else{
                logger.debug(title);
                logger.debug(DateUtil.dateToString(date));
            }

        }
    }
}
