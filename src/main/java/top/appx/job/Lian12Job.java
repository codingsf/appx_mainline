package top.appx.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.appx.entity.Article;
import top.appx.factory.ArticleFactory;
import top.appx.service.ArticleService;
import top.appx.util.HttpUtil;

import java.util.Date;
@Component
public class Lian12Job {

    @Autowired
    private ArticleService articleService;

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public void execute()throws Exception{
        String str=  HttpUtil.httpPost("https://www.12lian.com/12lian/announce/pageList","");

        JSONArray arr = JSONObject.parseObject(str).getJSONArray("attachment");
        for (int i = 0; i < arr.size(); i++) {
            if(i>=3){
                break;
            }
            JSONObject jsonObject = arr.getJSONObject(i);
            String title =jsonObject.getString("title");
            String url = "https://www.12lian.com/announce/preview/"+jsonObject.getString("announceId");
            String content = jsonObject.getString("content");
            Date createTime = jsonObject.getDate("createTime");
            Article article =  ArticleFactory.createArticle("notice_12lian",title,content,url,createTime);

            if(articleService!=null){
                articleService.saveIfNotExistUrl(article);
            }else{
                System.out.println(article.getTitle());
            }
        }

    }
}
