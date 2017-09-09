package top.appx.service;

import top.appx.entity.Goods;
import top.appx.entity.vo.GoodsVO;

import java.util.List;

public interface GoodsService {
    List<Goods> findByUser(long userId);
    List<GoodsVO> findVO(Goods goods);
    void save(Goods goods);

    Goods findById(long id);

    void update(Goods goods);

    void buy(Goods goods,long userId);
}
