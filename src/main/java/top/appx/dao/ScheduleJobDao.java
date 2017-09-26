package top.appx.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import top.appx.entity.ScheduleJob;

@Component
@Mapper
public interface ScheduleJobDao extends BaseDao<ScheduleJob> {
    ScheduleJob findByName(String name);
    void updateExecuteResult(ScheduleJob scheduleJob);
}
