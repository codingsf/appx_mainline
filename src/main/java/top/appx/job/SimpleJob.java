package top.appx.job;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import top.appx.entity.Article;
import top.appx.factory.ArticleFactory;
import top.appx.job.vo.JobParam;
import top.appx.service.ArticleService;
import top.appx.zutil.DateUtil;

import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SimpleJob {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    protected Map<String,String> cookies = new HashMap<>();


    protected JobParam jobParam;
    public void setJobParam(JobParam jobParam){
        this.jobParam = jobParam;
    }

    @Autowired
    private ArticleService articleService;
    public SimpleJob(){

    }
    public SimpleJob(JobParam jobParam){
        this.jobParam = jobParam;
    }
    public void deal(String url)throws Exception{
        Element doc = Jsoup.connect(url).cookies(cookies).get();

        String title = null;
        Date date = null;
        String content =  null;

        try{
            title = doc.select(jobParam.getTitleSel()).first().html();
        }catch (Exception ex){
            logger.info("获取文章标题出错",ex);
            title = "未知标题";
        }

        try {
            String timeStr = doc.select(jobParam.getTimeSel()).first().html();
            date = DateUtil.parse(timeStr);
        }catch (Exception ex){
            logger.error("获取文章发表时间出错",ex);
        }

        try{
            content = doc.select(jobParam.getContentSel()).first().html();
        }catch (Exception ex){
            logger.info("获取文章发表内容出错",ex);
        }

        if(articleService!=null) {
            Article article = ArticleFactory.createArticle(jobParam.getArticleGroupFlag(), title, content, url, date);
            articleService.saveIfNotExistUrl(article);
        }else{
            logger.debug(title);
            logger.debug(DateUtil.dateToString(date));
        }

    }

    public void execute()throws Exception{

        logger.debug("execute ============== "+this.jobParam.getArticleGroupFlag());
        Element doc = Jsoup.connect(jobParam.getListUrl())
                .cookies(cookies)
                .get();

        Elements elements = doc.select(jobParam.getNoticeASel());

        if(elements.size()==0){
            logger.error("没有获取到公告,"+jobParam.getArticleGroupFlag());
            if(doc.html().contains("您的访问过于频繁，为确认本次访问为正常用户行为，需要您协助验证")){
                logger.error("微信获取需要验证码");
            }
        }
        for (Element element : elements) {
            String href = element.attr("href");
            URI base=new URI(jobParam.getListUrl());//基本网页URI
            URI abs=base.resolve(href);//解析于上述网页的相对URL，得到绝对URI
            URL absURL=abs.toURL();//转成URL
            href = absURL.toString();
            deal(href);
        }
    }
}
