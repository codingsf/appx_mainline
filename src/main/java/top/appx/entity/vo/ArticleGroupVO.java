package top.appx.entity.vo;

import top.appx.entity.ArticleGroup;

public class ArticleGroupVO extends ArticleGroup {
    private boolean subscribe;
    private long total;


    public boolean isSubscribe() {
        return subscribe;
    }

    public void setSubscribe(boolean subscribe) {
        this.subscribe = subscribe;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
