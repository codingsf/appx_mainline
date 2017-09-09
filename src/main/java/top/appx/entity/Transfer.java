package top.appx.entity;

import java.util.Date;

public class Transfer extends BaseEntity {
    private static final long serialVersionUID = 6134314253252154622L;
    private Long id;
    private Long sUserId;
    private Long dUserId;
    private String description;
    private Date createTime;
    private double money;
    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getsUserId() {
        return sUserId;
    }

    public void setsUserId(Long sUserId) {
        this.sUserId = sUserId;
    }

    public Long getdUserId() {
        return dUserId;
    }

    public void setdUserId(Long dUserId) {
        this.dUserId = dUserId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
