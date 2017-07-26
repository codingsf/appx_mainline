package top.appx.entity;

import java.util.Date;

public class ArticleSubscribe extends BaseEntity {
    private Long id;
    private Long aritcleGroupId;
    private Long userId;
    private Date createTime;
    private int noticeType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAritcleGroupId() {
        return aritcleGroupId;
    }

    public void setAritcleGroupId(Long aritcleGroupId) {
        this.aritcleGroupId = aritcleGroupId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(int noticeType) {
        this.noticeType = noticeType;
    }
}
