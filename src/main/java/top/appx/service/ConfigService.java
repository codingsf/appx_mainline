package top.appx.service;

import com.github.pagehelper.PageInfo;
import top.appx.entity.Config;

import java.util.List;

public interface ConfigService {
    PageInfo<Config> findPage(int pageNum,int pageSize);
    void save(Config config);
    void update(Config config);
    void delete(List<Long> ids);
}
