package top.appx.job;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.appx.entity.Article;
import top.appx.factory.ArticleFactory;
import top.appx.service.ArticleService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component
public class JubiNewTop {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ArticleService articleService;
    List<String> list = new ArrayList<>();
    public void execute()throws Exception{
        logger.info("执行横幅检测,size="+list.size());
        Document doc = Jsoup.connect("https://www.jubi.com").get();
        Elements elements = doc.select(".my-carousel-inner .item");
        List<String> strArr = new ArrayList<>();
        for (Element element : elements) {
            String str = element.outerHtml();
            strArr.add(str);
        }
        if(list.size()==0){
            list.addAll(strArr);
        }else{
            for (String s : strArr) {
                if(!list.contains(s)){
                    list.add(s);
                    if(articleService!=null) {
                        String title = "聚币网有新的横幅产生";
                        String content ="软件检测到"+ title;
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
