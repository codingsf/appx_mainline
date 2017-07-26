package top.appx.entity.vo;

import top.appx.entity.CollectParam;
import top.appx.entity.ScheduleJob;

import java.io.Serializable;

public class QrtzJob implements Serializable {
    private static final long serialVersionUID = 6238347189535254907L;

    private ScheduleJob scheduleJob;
    private CollectParam collectParam;
    private String type;

    public String getCron(){
        switch (type){
            case "scheduleJob":
                return scheduleJob.getCron();
            case "collectParam":
                return collectParam.getCron();
            default:
                return null;
        }
    }
    public boolean isAsync() {
        switch (type){
            case "scheduleJob":
                return scheduleJob.isAsync();
            case "collectParam":
                return true;
            default:
                    return true;
        }
    }

    public String getName(){
        switch(type){
            case "scheduleJob":
                return scheduleJob.getName();
            case "collectParam":
                return collectParam.getName();
            default:
                return null;
        }
    }
    public String getGroupName(){
        switch (type){
            case "scheduleJob":
                return scheduleJob.getGroupName();
            case "collectParam":
                return "采集";
            default:
                return null;
        }
    }
    public Integer getStatus() {
        switch (type){
            case "scheduleJob":
                return scheduleJob.getStatus();
            case "collectParam":
                return collectParam.getStatus();
            default:
                return null;
        }
    }


    public QrtzJob(ScheduleJob scheduleJob){
        this.scheduleJob = scheduleJob;
        type = "scheduleJob";
    }
    public QrtzJob(CollectParam collectParam){
        this.collectParam = collectParam;
        type = "collectParam";
    }



    public ScheduleJob getScheduleJob() {
        return scheduleJob;
    }

    public void setScheduleJob(ScheduleJob scheduleJob) {
        this.scheduleJob = scheduleJob;
    }

    public CollectParam getCollectParam() {
        return collectParam;
    }

    public void setCollectParam(CollectParam collectParam) {
        this.collectParam = collectParam;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



}
