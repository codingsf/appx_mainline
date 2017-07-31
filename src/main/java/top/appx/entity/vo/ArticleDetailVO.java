package top.appx.entity.vo;

import top.appx.entity.Article;

public class ArticleDetailVO extends Article {
    private String articleGroupName;

    public String getArticleGroupName() {
        return articleGroupName;
    }

    public void setArticleGroupName(String articleGroupName) {
        this.articleGroupName = articleGroupName;
    }
}
