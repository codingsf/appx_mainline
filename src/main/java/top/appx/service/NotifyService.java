package top.appx.service;

import com.github.pagehelper.PageInfo;
import top.appx.entity.Notify;

import java.util.List;

public interface NotifyService {
    void save(Notify notify);

    PageInfo<Notify> findPage(Object search,int pageNum, int pageSize);

    void delete(List<Long> ids);
}
