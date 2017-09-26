package top.appx.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.appx.dao.NotifyDao;
import top.appx.entity.Notify;
import top.appx.service.NotifyService;

import java.util.Date;
import java.util.List;

@Service
public class NotifyServiceImpl implements NotifyService {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private NotifyDao notifyDao;


    @Override
    public boolean save(Notify notify) {
        logger.info("发送消息 ="+notify);
        notify.setStatus("wait");
        notify.setCreateTime(new Date());
        notifyDao.insert(notify);
        return true;
    }

    @Override
    public PageInfo<Notify> findPage(Object search,int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Notify> notifyList = notifyDao.find(search);
        return new PageInfo<Notify>(notifyList);
    }

    @Override
    public void delete(List<Long> ids) {
        notifyDao.deleteByPrimaryKeys(ids);
    }

    @Override
    public List<Notify> waitDeal() {
        return notifyDao.waitDeal();
    }

    @Override
    public void dealResult(Notify notify) {
        notifyDao.updateDealResult(notify);
    }
}
