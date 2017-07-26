package top.appx.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.quartz.CronTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.appx.dao.ScheduleJobDao;
import top.appx.entity.ScheduleJob;
import top.appx.entity.vo.QrtzJob;
import top.appx.exception.NameExistException;
import top.appx.service.ScheduleJobService;
import top.appx.util.ScheduleUtil;

import java.util.Date;
import java.util.List;

@Service
public class ScheduleJobServiceImpl implements ScheduleJobService {
    @Autowired
    private ScheduleJobDao scheduleJobDao;

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;


    @Override
    public void save(ScheduleJob scheduleJob) {
        if(scheduleJobDao.findByName(scheduleJob.getName())!=null){
            throw new NameExistException();
        }

        QrtzJob qrtzJob = new QrtzJob(scheduleJob);
        ScheduleUtil.createQrtzJob(schedulerFactoryBean.getScheduler(), qrtzJob);


        Date now = new Date();
        scheduleJob.setCreateTime(now);
        scheduleJob.setModifyTime(now);

        scheduleJobDao.insert(scheduleJob);
    }

    @Override
    public PageInfo<ScheduleJob> findPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<ScheduleJob> list = scheduleJobDao.find();
        return new PageInfo<>(list);
    }

    @Override
    @Transactional
    public void update(ScheduleJob scheduleJob) {
        scheduleJob.setModifyTime(new Date());
        scheduleJobDao.updateByPrimaryKey(scheduleJob);

        ScheduleJob record = scheduleJobDao.selectByPrimaryKey(scheduleJob.getId());

        QrtzJob qrtzJob = new QrtzJob(record);
        //因为Quartz只能更新cron表达式，当更改了cron表达式以外的属性时，执行的逻辑是：先删除旧的再创建新的。注:equals排除了cron属性
        if(!scheduleJob.equals(record)){
            //删除旧的任务
            ScheduleUtil.deleteScheduleJob(schedulerFactoryBean.getScheduler(), qrtzJob.getName(), qrtzJob.getGroupName());
            //创建新的任务,保持原来任务的状态
            scheduleJob.setStatus(record.getStatus());

            qrtzJob = new QrtzJob(scheduleJob);

            ScheduleUtil.createQrtzJob(schedulerFactoryBean.getScheduler(), qrtzJob);
        }else {
            //当cron表达式和原来不一致才做更新
            if(!scheduleJob.getCron().equals(record.getCron())){
                //更新调度任务
                qrtzJob = new QrtzJob(scheduleJob);
                ScheduleUtil.updateScheduleJob(schedulerFactoryBean.getScheduler(), qrtzJob);
            }
        }


    }

    @Override
    public void deleteByIds(List<Long> ids) {
        for (Long id : ids) {
            ScheduleJob scheduleJob = scheduleJobDao.selectByPrimaryKey(id);
            scheduleJobDao.deleteByPrimaryKey(id);
            ScheduleUtil.deleteScheduleJob(schedulerFactoryBean.getScheduler(), scheduleJob.getName(), scheduleJob.getGroupName());
        }
    }

    @Override
    public void initScheduleJob() {
        List<ScheduleJob> scheduleJobList = scheduleJobDao.find();
        for (ScheduleJob scheduleJob : scheduleJobList) {
            QrtzJob qrtzJob = new QrtzJob(scheduleJob);
            CronTrigger cronTrigger = ScheduleUtil.getCronTrigger(schedulerFactoryBean.getScheduler(), qrtzJob.getName(), qrtzJob.getGroupName());

            if(cronTrigger==null){
                ScheduleUtil.createQrtzJob(schedulerFactoryBean.getScheduler(), qrtzJob);
            }
            else{
                ScheduleUtil.updateScheduleJob(schedulerFactoryBean.getScheduler(), qrtzJob);
            }
        }

    }


}
