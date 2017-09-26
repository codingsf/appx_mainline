package top.appx.util;

import org.quartz.*;
import top.appx.entity.vo.QrtzJob;
import top.appx.common.Constant;
import top.appx.config.task.AsyncJobFactory;
import top.appx.config.task.ScheduleExecute;
import top.appx.config.task.SyncJobFactory;
import top.appx.entity.ScheduleJob;

public class ScheduleUtil {
    /**
     * 获取表达式触发器
     *
     * @param scheduler the scheduler
     * @param jobName the job name
     * @param jobGroup the job group
     * @return cron trigger
     */
    public static CronTrigger getCronTrigger(Scheduler scheduler, String jobName, String jobGroup) {

        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
            return (CronTrigger) scheduler.getTrigger(triggerKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new RuntimeException("获取定时任务CronTrigger出现异常");
        }
    }

    public static void updateScheduleJob(Scheduler scheduler, QrtzJob qrtzJob) {
        updateScheduleJob(scheduler, qrtzJob.getName(), qrtzJob.getGroupName(),
                qrtzJob.getCron());
    }

    /**
     * 更新定时任务的cron表达式
     *
     * @param scheduler the scheduler
     * @param jobName the job name
     * @param jobGroup the job group
     * @param cronExpression the cron expression
     */
    public static void updateScheduleJob(Scheduler scheduler, String jobName, String jobGroup,
                                         String cronExpression) {


        try {

            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);

            //表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

            //按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            // 忽略状态为PAUSED的任务，解决集群环境中在其他机器设置定时任务为PAUSED状态后，集群环境启动另一台主机时定时任务全被唤醒的bug
            if(!triggerState.name().equalsIgnoreCase("PAUSED")){
                //按新的trigger重新设置job执行
                scheduler.rescheduleJob(triggerKey, trigger);
            }


        } catch (SchedulerException e) {
            throw new RuntimeException("更新定时任务失败");
        }
    }

    public static void createQrtzJob(Scheduler scheduler, QrtzJob qrtzJob) {
        createQrtzJob(scheduler,qrtzJob.getName(),qrtzJob.getGroupName(),qrtzJob.getCron(),qrtzJob.isAsync(),qrtzJob);
    }


    /**
     * 暂停任务
     *
     * @param scheduler
     * @param jobName
     * @param jobGroup
     */
    public static void pauseJob(Scheduler scheduler, String jobName, String jobGroup) {

        JobKey jobKey = getJobKey(jobName, jobGroup);
        try {
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            throw new RuntimeException("暂停定时任务失败");
        }
    }

    /**
     * 恢复任务
     *
     * @param scheduler
     * @param jobName
     * @param jobGroup
     */
    public static void resumeJob(Scheduler scheduler, String jobName, String jobGroup) {

        JobKey jobKey = getJobKey(jobName, jobGroup);
        try {
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            throw new RuntimeException("暂停定时任务失败");
        }
    }


    /**
     * 获取jobKey
     *
     * @param jobName the job name
     * @param jobGroup the job group
     * @return the job key
     */
    public static JobKey getJobKey(String jobName, String jobGroup) {

        return JobKey.jobKey(jobName, jobGroup);
    }



    /**
     * 创建定时任务
     *
     * @param scheduler the scheduler
     * @param jobName the job name
     * @param jobGroup the job group
     * @param cronExpression the cron expression
     * @param isAsync the is isAsync
     * @param qrtzJob the param
     */
    public static void createQrtzJob(Scheduler scheduler, String jobName, String jobGroup,
                                     String cronExpression, Boolean isAsync, QrtzJob qrtzJob) {
        try {
            //同步或异步
            Class<? extends Job> jobClass = isAsync ? AsyncJobFactory.class : SyncJobFactory.class;

            //构建job信息 (任务名，任务组，任务执行类)
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName , jobGroup).build();

            //表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

            //按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup).withSchedule(scheduleBuilder).build();

            //放入参数，运行时的方法可以获取
            jobDetail.getJobDataMap().put(ScheduleJob.JOB_PARAM_KEY, qrtzJob);

            scheduler.scheduleJob(jobDetail, trigger);

            //暂停任务 (如果修改修改任务时，选择先删除再创建，保持原来的任务状态)
            if(qrtzJob.getStatus()!=null && Constant.JobStatus.pause.getValue()== qrtzJob.getStatus()){
                pauseJob(scheduler, qrtzJob.getName(), qrtzJob.getGroupName());
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new RuntimeException("创建定时任务失败");
        }
    }

    public static void deleteScheduleJob(Scheduler scheduler, String jobName, String jobGroup,Long collectParamId) {
        String keyName = "collectParam_"+collectParamId;
        ScheduleExecute.objMap.remove(keyName);
        deleteScheduleJob(scheduler,jobName,jobGroup);
    }
    /**
     * 删除定时任务
     *
     * @param scheduler
     * @param jobName
     * @param jobGroup
     */
    public static void deleteScheduleJob(Scheduler scheduler, String jobName, String jobGroup) {
        try {


            scheduler.deleteJob(getJobKey(jobName, jobGroup));

        } catch (SchedulerException e) {
          //  log.error("删除定时任务失败", e);
            throw new RuntimeException("删除定时任务失败");
        }
    }


}
