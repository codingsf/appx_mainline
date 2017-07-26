package top.appx.service;

import com.github.pagehelper.PageInfo;
import top.appx.entity.CollectParam;

import java.util.List;

public interface CollectParamService {
    PageInfo<CollectParam> findPage(int pageNum, int pageSize);

    void save(CollectParam collectParam);

    void deleteByIds(List<Long> ids);

    void update(CollectParam collectParam);

    void initCollectParamQrtzJob();
}
