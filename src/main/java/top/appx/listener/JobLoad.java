package top.appx.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.appx.service.CollectParamService;
import top.appx.service.ScheduleJobService;

import javax.annotation.PostConstruct;

@Component
public class JobLoad {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ScheduleJobService  scheduleJobService;


    @Autowired
    private CollectParamService collectParamService;
    @PostConstruct
    public void init(){

        logger.info("初始化定时任务开始");

        scheduleJobService.initScheduleJob();

        collectParamService.initCollectParamQrtzJob();

        logger.info("初始化定时任务结束");


    }
}
