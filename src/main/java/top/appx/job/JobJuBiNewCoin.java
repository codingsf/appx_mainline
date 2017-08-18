package top.appx.job;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import top.appx.entity.Article;
import top.appx.factory.ArticleFactory;
import top.appx.service.ArticleService;
import top.appx.service.NotifyService;
import top.appx.util.DateUtil;
import top.appx.util.HttpUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Component
public class JobJuBiNewCoin {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private ArticleService articleService;

    List<String> list = new ArrayList<>();
    public void execute(){
        logger.info("执行上新检测,size="+list.size());
        String str = HttpUtil.httpGet("https://www.jubi.com/coin/allcoin");
        JSONObject doc = JSONObject.parseObject(str);
        if(list.size()==0){
            list.addAll(doc.keySet());
        }else {
            for (String s : doc.keySet()) {
                if(!list.contains(s)){
                    list.add(s);
                    if(articleService!=null) {
                        String coinName = doc.getJSONArray(s).get(0).toString();
                        String title = coinName+"上线聚币";
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
}
