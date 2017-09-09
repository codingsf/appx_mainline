package top.appx.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.appx.dao.BtcOrderDao;
import top.appx.entity.BtcOrder;
import top.appx.service.BtcOrderService;

@Service
public class BtcOrderServiceImpl implements BtcOrderService {

    @Autowired
    private BtcOrderDao btcOrderDao;

    @Override
    public void insert(BtcOrder btcOrder) {
        if(!btcOrderDao.exist(btcOrder)){
            btcOrderDao.insert(btcOrder);
        }
    }
}
