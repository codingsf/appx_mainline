package top.appx.factory;

import top.appx.entity.Article;

import java.util.Date;

public class ArticleFactory {
    public static Article createArticle(String flag,String title, String content, String url,Date occTime){
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setUrl(url);
        article.setCreateTime(new Date());
        article.setType("reprint");//转载
        article.setArticleGroupFlag(flag);
        article.setOccTime(occTime);
        return article;
    }
}
