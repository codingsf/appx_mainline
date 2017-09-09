package top.appx.dao;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import top.appx.entity.Goods;
import top.appx.entity.vo.GoodsVO;

import java.util.List;
@Component
@Mapper
public interface GoodsDao extends BaseDao<Goods> {
    List<Goods> findByUser(long userId);
    List<GoodsVO> findVO(Goods search);
    void updateNum(Goods goods0);
}
