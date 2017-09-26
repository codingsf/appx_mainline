package top.appx.config.task;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import top.appx.entity.vo.QrtzJob;
import top.appx.entity.ScheduleJob;

/**
 * 同步任务工厂(若一个方法一次执行不完下次轮转时则等待改方法执行完后才执行下一次操作)
 * @author cuiP
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SyncJobFactory extends QuartzJobBean {

    protected static final Logger log = LoggerFactory.getLogger(SyncJobFactory.class);

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
         //   log.info("SyncJobFactory execute,执行开始...");
            //获取任务记录
            QrtzJob qrtzJob = (QrtzJob) context.getMergedJobDataMap().get(ScheduleJob.JOB_PARAM_KEY);
            //获取spring上下文
            ApplicationContext applicationContext = (ApplicationContext)context.getScheduler().getContext().get("applicationContextKey");
            //执行调度任务
            ScheduleExecute.execute(applicationContext, qrtzJob);

           // log.info("执行结束");
        } catch (SchedulerException e) {
            log.error("SyncJobFactory execute,执行失败...");
        }catch (Exception ex){
            log.error("执行出错:"+ex.getMessage());
        }
    }

}