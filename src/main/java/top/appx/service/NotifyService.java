package top.appx.service;

import com.github.pagehelper.PageInfo;
import top.appx.entity.Notify;

import java.util.List;

public interface NotifyService {
    boolean save(Notify notify);

    PageInfo<Notify> findPage(Object search,int pageNum, int pageSize);

    void delete(List<Long> ids);

    List<Notify> waitDeal();

    void dealResult(Notify notify);
}
