package top.appx.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import top.appx.entity.BtcOrder;
@Component
@Mapper
public interface BtcOrderDao {
    void insert(BtcOrder btcOrder);
    boolean exist(BtcOrder btcOrder);
}
