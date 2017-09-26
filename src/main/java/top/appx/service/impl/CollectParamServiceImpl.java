package top.appx.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.quartz.CronTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.appx.entity.CollectParam;
import top.appx.entity.vo.QrtzJob;
import top.appx.service.CollectParamService;
import top.appx.dao.CollectParamDao;
import top.appx.util.ScheduleUtil;

import java.util.Date;
import java.util.List;

@Service
public class CollectParamServiceImpl implements CollectParamService {
    @Autowired
    private CollectParamDao collectParamDao;

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Override
    public PageInfo<CollectParam> findPage(Object search, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<CollectParam> collectParamList =collectParamDao.find(search);
        return new PageInfo<CollectParam>(collectParamList);
    }

    @Transactional
    @Override
    public void save(CollectParam collectParam) {
        collectParamDao.insertAutoSetId(collectParam);

        QrtzJob qrtzJob = new QrtzJob(collectParam);
        ScheduleUtil.createQrtzJob(schedulerFactoryBean.getScheduler(), qrtzJob);
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        for (Long id : ids) {
            CollectParam collectParam = collectParamDao.selectByPrimaryKey(id);
            collectParamDao.deleteByPrimaryKey(id);
            ScheduleUtil.deleteScheduleJob(schedulerFactoryBean.getScheduler(), collectParam.getName(), "采集");
        }
    }

    @Override
    public void update(CollectParam collectParam) {
        {

            CollectParam record = collectParamDao.selectByPrimaryKey(collectParam.getId());

            record.setCookieStr(collectParam.getCookieStr());
            QrtzJob qrtzJob = new QrtzJob(record);
            //删除旧的任务
            ScheduleUtil.deleteScheduleJob(schedulerFactoryBean.getScheduler(), qrtzJob.getName(), qrtzJob.getGroupName(),record.getId());
            //创建新的任务,保持原来任务的状态

            qrtzJob = new QrtzJob(collectParam);
            ScheduleUtil.createQrtzJob(schedulerFactoryBean.getScheduler(), qrtzJob);


            collectParam.setModifyTime(new Date());
            collectParamDao.updateByPrimaryKey(collectParam);
        }


    }

    @Override
    public void initCollectParamQrtzJob() {
        List<CollectParam> list = collectParamDao.find();
        for (CollectParam collectParam : list) {
            QrtzJob qrtzJob = new QrtzJob(collectParam);
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
