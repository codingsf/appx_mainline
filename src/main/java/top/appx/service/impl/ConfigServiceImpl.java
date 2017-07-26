package top.appx.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.appx.dao.ConfigDao;
import top.appx.entity.Config;
import top.appx.service.ConfigService;

import java.util.Date;
import java.util.List;

@Service
public class ConfigServiceImpl implements ConfigService {
    @Autowired
    private ConfigDao configDao;

    @Override
    public PageInfo<Config> findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Config> articleList = configDao.find();
        return new PageInfo<Config>(articleList);
    }

    @Override
    public void save(Config config) {
        config.setCreateTime(new Date());
        configDao.insert(config);
    }

    @Override
    public void update(Config config) {
        config.setModifyTime(new Date());
        configDao.updateByPrimaryKey(config);
    }

    @Override
    public void delete(List<Long> ids) {
        configDao.deleteByPrimaryKeys(ids);
    }
}
