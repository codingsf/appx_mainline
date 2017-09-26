package top.appx.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.appx.entity.Article;
import top.appx.factory.ArticleFactory;
import top.appx.service.ArticleService;
import top.appx.zutil.HttpUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ChbtcJob {
    @Autowired
   private ArticleService articleService;
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private List<String> list = new ArrayList<>();

    public void execute(){

        String str =  HttpUtil.httpGet("https://trans.chbtc.com/line/topall");

        str = str.substring(1,str.length()-1);
        JSONObject jsonObject = JSONObject.parseObject(str);

        JSONArray datas = jsonObject.getJSONArray("datas");

        List<String> tmp = new ArrayList<>();

        for (int i = 0; i < datas.size(); i++) {
            JSONObject jsonObject1 = datas.getJSONObject(i);
            tmp.add(jsonObject1.getString("market"));
        }

        if(list.size()==0){
            list.addAll(tmp);
        }

      // logger.info("中比特上新检测,size="+list.size());
        for (String s : tmp) {
            if(!list.contains(s)){
                list.add(s);
                if(articleService!=null) {
                    String title = s+"上线中比特";
                    logger.info(title);
                    String content = "软件检测到"+title;
                    String url = null;
                    Date date = new Date();
                    Article article = ArticleFactory.createArticle("notice_newsappx", title, content, url, date);
                    articleService.saveIfNotExistUrl(article);
                }
            }
        }

    }
}
