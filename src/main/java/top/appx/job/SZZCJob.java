package top.appx.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.appx.entity.Article;
import top.appx.factory.ArticleFactory;
import top.appx.service.ArticleService;
import top.appx.util.DateUtil;
import top.appx.util.HttpUtil;

import java.util.Date;

@Component
public class SZZCJob {
    @Autowired
   private ArticleService articleService;
    public void execute(){

        String str =  HttpUtil.httpGet("https://szzc.com/api/news/articles/NOTICE?language=zh");

        JSONObject jsonObject = JSONObject.parseObject(str);
        JSONArray data = jsonObject.getJSONObject("result").getJSONArray("data");

        JSONObject item = data.getJSONObject(0);
        String title = item.get("subject").toString();
        String idStr = item.get("id").toString();

        String str1 = HttpUtil.httpGet("https://szzc.com/api/news/article/"+idStr);
        JSONObject jsonObject1 = JSONObject.parseObject(str1);
        jsonObject1 = jsonObject1.getJSONArray("result").getJSONObject(0);
        String content = jsonObject1.getString("content");
        Date occTime = jsonObject1.getDate("publication_date");

        String url = "https://www.szzc.com/#!/news/"+idStr;

        Article article = ArticleFactory.createArticle("notice_szzc", title, content, url, occTime);
        if(articleService!=null){
            articleService.saveIfNotExistUrl(article);
        }

    }
}
