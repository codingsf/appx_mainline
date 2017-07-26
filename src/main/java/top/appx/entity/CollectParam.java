package top.appx.entity;

import java.util.Date;

public class CollectParam extends BaseEntity {
    private static final long serialVersionUID = 6234342183534214923L;

    private Long id;
    private String listUrl;//切入点
    private String articleASel;//文章选择器
    private String timeSel;//时间选择器
    private String titleSel;//标题选择器
    private String contentSel;//内容选择器
    private String articleGroupFlag;//文章分组
    private String type;//类型common,普通;wechat,微信
    private String cookieStr;//需要用到的cookie
    private Date lastErrorTime;//最后执行失败时间
    private Date lastSuccessTime;//最后执行成功时间
    private Date createTime;
    private Date modifyTime;
    private String name;
    private String remark;
    private String cron;
    private Integer status;
    private String twitterName;
    private String errorMsg;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getListUrl() {
        return listUrl;
    }

    public void setListUrl(String listUrl) {
        this.listUrl = listUrl;
    }

    public String getArticleASel() {
        return articleASel;
    }

    public void setArticleASel(String articleASel) {
        this.articleASel = articleASel;
    }

    public String getTimeSel() {
        return timeSel;
    }

    public void setTimeSel(String timeSel) {
        this.timeSel = timeSel;
    }

    public String getTitleSel() {
        return titleSel;
    }

    public void setTitleSel(String titleSel) {
        this.titleSel = titleSel;
    }

    public String getContentSel() {
        return contentSel;
    }

    public void setContentSel(String contentSel) {
        this.contentSel = contentSel;
    }

    public String getArticleGroupFlag() {
        return articleGroupFlag;
    }

    public void setArticleGroupFlag(String articleGroupFlag) {
        this.articleGroupFlag = articleGroupFlag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCookieStr() {
        return cookieStr;
    }

    public void setCookieStr(String cookieStr) {
        this.cookieStr = cookieStr;
    }

    public Date getLastErrorTime() {
        return lastErrorTime;
    }

    public void setLastErrorTime(Date lastErrorTime) {
        this.lastErrorTime = lastErrorTime;
    }

    public Date getLastSuccessTime() {
        return lastSuccessTime;
    }

    public void setLastSuccessTime(Date lastSuccessTime) {
        this.lastSuccessTime = lastSuccessTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTwitterName() {
        return twitterName;
    }

    public void setTwitterName(String twitterName) {
        this.twitterName = twitterName;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
