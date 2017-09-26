package top.appx.entity;

import java.util.Date;

public class ScheduleJob extends BaseEntity {

    //public static Long serialVersionUID = 6247347289585252907L;
    private static final long serialVersionUID = 6247347289585252907L;

    /** 任务调度的参数key */
    public static final String JOB_PARAM_KEY    = "jobParam";

    private Long id;
    private String name;
    private String groupName;
    private String cron;
    private String beanClass;
    private String methodName;
    private String params;
    private Integer status;
    private String remark;
    private Date createTime;
    private Date modifyTime;
    private boolean isAsync;
    private Date lastErrorTime;
    private Date lastSuccessTime;
    private String errorMsg;

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

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(String beanClass) {
        this.beanClass = beanClass;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public boolean isAsync() {
        return isAsync;
    }

    public void setAsync(boolean async) {
        isAsync = async;
    }
}
