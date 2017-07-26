package top.appx.entity;

import java.util.Date;

public class Article extends BaseEntity {
    private Long id;
    private String title;
    private String content;
    private Date createTime;
    private Date modifyTime;
    private String type;//notice,官方发布,simple普通,reprint转载
    private String url;//转载url
    private Long articleGroupId;//分组
    private String articleGroupFlag;
    private Date occTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public Long getArticleGroupId() {
        return articleGroupId;
    }

    public void setArticleGroupId(Long articleGroupId) {
        this.articleGroupId = articleGroupId;
    }

    public String getArticleGroupFlag() {
        return articleGroupFlag;
    }

    public void setArticleGroupFlag(String articleGroupFlag) {
        this.articleGroupFlag = articleGroupFlag;
    }

    public Date getOccTime() {
        return occTime;
    }

    public void setOccTime(Date occTime) {
        this.occTime = occTime;
    }
}
