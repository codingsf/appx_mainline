package top.appx.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import top.appx.entity.Notify;

import java.util.List;

@Component
@Mapper
public interface NotifyDao extends BaseDao<Notify> {

    List<Notify> waitDeal();

    void updateDealResult(Notify notify);
}
