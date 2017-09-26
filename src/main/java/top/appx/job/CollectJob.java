package top.appx.job;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import top.appx.dao.CollectParamDao;
import top.appx.entity.Article;
import top.appx.entity.CollectParam;
import top.appx.exception.ArticleASelNotFoundException;
import top.appx.exception.CookieException;
import top.appx.factory.ArticleFactory;
import top.appx.service.ArticleService;
import top.appx.service.CookieService;
import top.appx.zutil.DateUtil;
import top.appx.zutil.StringUtil;

import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CollectJob {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    protected CollectParam collectParam;

    protected ArticleService articleService;
    protected ApplicationContext applicationContext;
    protected CollectParamDao collectParamDao;
    private CookieService cookieService;


    protected Map<String,String> getCookies(String host){

        Map<String,String> cookies = new HashMap<>();
        String flag =  null;
        String cookieStr = null;
        if(StringUtil.isNullOrEmpty(collectParam.getCookieStr())){
            if(host.contains("weixin.sogou.com")){
                flag = "sogou_wechat";
            }
            else if(host.contains("mp.weixin.qq.com/profile")){
                flag = "wechat";
            }
            if(flag==null){
                return cookies;
            }
            cookieStr = cookieService.findAndMrak(flag).getStr();
        }
        else{
            cookieStr = collectParam.getCookieStr();
        }

        if(StringUtil.isNullOrEmpty(cookieStr)){
            return cookies;
        }
        String[] arr = cookieStr.split(";");
        for (String s : arr) {
            String key = s.split("=")[0].trim();
            String value = s.split("=")[1].trim();
            cookies.put(key, value);
        }
        return cookies;
    }

    public CollectJob(CollectParam collectParam,ApplicationContext applicationContext){
        this.collectParam = collectParam;
        this.applicationContext = applicationContext;
        if(applicationContext!=null) {
            this.articleService = applicationContext.getBean(ArticleService.class);
            this.collectParamDao = applicationContext.getBean(CollectParamDao.class);
            this.cookieService = applicationContext.getBean(CookieService.class);
        }
    }


    protected void deal(String url)throws Exception{

        if(articleService!=null){
            if(articleService.existUrl(url)){
                return;
            }
        }


        Element doc = Jsoup.connect(url)
                .cookies(getCookies(url))
                .get();

        String title = null;
        Date date = null;
        String content =  null;

        try{
            title = doc.select(collectParam.getTitleSel()).first().html();
        }catch (Exception ex){
            logger.error(collectParam.getName()+" 获取文章标题出错",ex);
            title = "未知标题";
        }

        if(!StringUtil.isNullOrEmpty(collectParam.getTimeSel())){
            try {
                String timeStr = doc.select(collectParam.getTimeSel()).first().html();
                date = DateUtil.parse(timeStr);
            }catch (Exception ex){
                logger.info(collectParam.getName()+" 获取文章发表时间出错",ex);
            }
        }


        try{

            Element element = doc.select(collectParam.getContentSel()).first();
            for (Element a : element.select("a")) {
                URI base=new URI(url);//基本网页URI
                URI abs=base.resolve(a.attr("href"));//解析于上述网页的相对URL，得到绝对URI
                URL absURL=abs.toURL();//转成URL
                String href = absURL.toString();
                a.attr("href",href);
            }

            content = doc.select(collectParam.getContentSel()).first().html();
        }catch (Exception ex){
            logger.info(collectParam.getName()+" 获取文章发表内容出错",ex);
        }

        if(articleService!=null) {
            Article article = ArticleFactory.createArticle(collectParam.getArticleGroupFlag(), title, content, url, date);
            articleService.saveIfNotExistUrl(article);
        }else{
            logger.debug(title);
            logger.debug(DateUtil.dateToString(date));
        }
    }
    public void execute()throws Exception{
        {
       //     logger.debug("execute ============== "+this.collectParam.getName());

            Element doc = Jsoup.connect(collectParam.getListUrl())
                    .cookies(getCookies(collectParam.getListUrl()))
                    .get();

            Elements elements = doc.select(collectParam.getArticleASel());

            if(elements.size()==0){
                if(doc.html().contains("您的访问过于频繁，为确认本次访问为正常用户行为，需要您协助验证")){
                    throw new CookieException("微信获取需要验证码");
                }
                throw new ArticleASelNotFoundException("获取不到文章");
            }
            for(int i=elements.size()-1;i>=0;i--){
                Element element = elements.get(i);
                String href = element.attr("href");
                URI base=new URI(collectParam.getListUrl());//基本网页URI
                URI abs=base.resolve(href);//解析于上述网页的相对URL，得到绝对URI
                URL absURL=abs.toURL();//转成URL
                href = absURL.toString();
                deal(href);
            }
        }

    }
}
