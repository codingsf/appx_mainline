package top.appx.config.task;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import top.appx.entity.ScheduleJob;
import top.appx.entity.vo.QrtzJob;

public class AsyncJobFactory extends QuartzJobBean {
    protected static final Logger log = LoggerFactory.getLogger(AsyncJobFactory.class);

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            Object obj  = context.getMergedJobDataMap().get(ScheduleJob.JOB_PARAM_KEY);

            QrtzJob qrtzJob = (QrtzJob) obj;

            //获取spring上下文
            ApplicationContext applicationContext = (ApplicationContext)context.getScheduler().getContext().get("applicationContextKey");

            //执行调度任务
            ScheduleExecute.execute(applicationContext, qrtzJob);
        } catch (SchedulerException e) {
            log.error("AsyncJobFactory execute,执行失败...");
        }catch (Exception ex){
            log.error("执行失败");
            ex.printStackTrace();
        }
    }
}
