package top.appx.controller.api;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.appx.controller.BaseController;
import top.appx.entity.ScheduleJob;
import top.appx.service.ScheduleJobService;

import java.util.List;

@RestController
@RequestMapping("/api/scheduleJobs")
public class Api_ScheduleJobController extends BaseController {

    @Autowired
    private ScheduleJobService scheduleJobService;

    @RequiresPermissions("scheduleJob:manager")
    @GetMapping
    public Object list(ScheduleJob search,@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        return scheduleJobService.findPage(search,pageNum,pageSize);
    }

    @RequiresPermissions("scheduleJob:add")
    @PostMapping
    public void add(ScheduleJob scheduleJob) {
        scheduleJobService.save(scheduleJob);
    }


    @RequiresPermissions("scheduleJob:modify")
    @PutMapping
    public void update(ScheduleJob scheduleJob){
        scheduleJobService.update(scheduleJob);

    }
    @RequiresPermissions("scheduleJob:del")
    @DeleteMapping("/{ids}")
    public void delete(@PathVariable("ids") List<Long> ids){
        scheduleJobService.deleteByIds(ids);
    }

}
