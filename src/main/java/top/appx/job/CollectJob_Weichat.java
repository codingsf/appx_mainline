package top.appx.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.context.ApplicationContext;
import top.appx.entity.Article;
import top.appx.entity.CollectParam;
import top.appx.exception.CookieException;
import top.appx.factory.ArticleFactory;
import top.appx.zutil.DateUtil;

import java.util.Date;

public class CollectJob_Weichat extends CollectJob{



    public CollectJob_Weichat(CollectParam collectParam, ApplicationContext applicationContext) {
        super(collectParam, applicationContext);
    }
    public CollectJob_Weichat(){
        super(null,null);
    }

    protected void deal(String url,Date date)throws Exception{
        Element doc = Jsoup.connect(url)
                .cookies(getCookies(url))
                .get();

        String title = doc.select("title").html();
        String content = doc.select("#js_content").html();

        if(articleService!=null) {
            Article article = ArticleFactory.createArticle(collectParam.getArticleGroupFlag(), title, content, url, date);
            articleService.saveIfNotExistUrl(article);
        }else{
            logger.debug(title);
            logger.debug(DateUtil.dateToString(date));
        }

    }
    public void execute()throws Exception{
        String listUrl = this.collectParam.getListUrl();
        Element doc = Jsoup.connect(listUrl)
                .cookies(getCookies(listUrl))
                .get();

        if(doc.html().contains("您的访问过于频繁，为确认本次访问为正常用户行为，需要您协助验证")){
            throw new CookieException("搜狗微信验证码");
        }


        String listUrl2 = doc.select(".tit a").first().attr("href");
        String html = Jsoup.connect(listUrl2)
                .cookies(getCookies(listUrl2))
                .get().html();

        if(html.contains("为了保护你的网络安全，请输入验证码")){
            throw new CookieException("微信需要验证码");
        }

        String startStr = "        var msgList = ";
        String endStr = "seajs.use(\"sougou/profile.js\");";
        int startIndex = html.indexOf(startStr)+startStr.length();
        int endIndex = html.indexOf(endStr);
        if(endIndex==-1 || startIndex==-1){
            System.out.println("wei");
        }
        String msgListStr = html.substring(startIndex,endIndex).trim();
        msgListStr = msgListStr.substring(0,msgListStr.length()-1);

        JSONObject jsonObject = JSONObject.parseObject(msgListStr);
        JSONArray list = jsonObject.getJSONArray("list");


        for(int i=0;i<list.size();i++){
            if(i>=3){
                break;
            }
            JSONObject item = list.getJSONObject(i);
            String url = item.getJSONObject("app_msg_ext_info").getString("content_url");
            url = "https://mp.weixin.qq.com"+ url.replace("&amp;","&");

            Date date = new Date(item.getJSONObject("comm_msg_info").getLong("datetime")*1000);
            deal(url,date);
        }

    }
}
