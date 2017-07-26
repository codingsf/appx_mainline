package top.appx.service;

import com.github.pagehelper.PageInfo;
import top.appx.entity.ScheduleJob;

import java.util.List;

public interface ScheduleJobService {
    void save(ScheduleJob scheduleJob);
    PageInfo<ScheduleJob> findPage(Integer pageNum , Integer pageSize);
    void update(ScheduleJob scheduleJob);

    void deleteByIds(List<Long> ids);

    void initScheduleJob();
}
