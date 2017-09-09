package top.appx.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.appx.dao.GoodsDao;
import top.appx.dao.TransferDao;
import top.appx.entity.Goods;
import top.appx.entity.vo.GoodsVO;
import top.appx.exception.MsgException;
import top.appx.service.GoodsService;
import top.appx.service.TransferService;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private TransferDao transferDao;

    @Autowired
    private TransferService transferService;

    @Override
    public List<Goods> findByUser(long userId) {
        return goodsDao.findByUser(userId);
    }

    @Override
    public List<GoodsVO> findVO(Goods goods) {
        return goodsDao.findVO(goods);
    }

    @Override
    public void save(Goods goods) {
        goodsDao.insert(goods);
    }

    @Override
    public Goods findById(long id) {
        return goodsDao.selectByPrimaryKey(id);
    }

    @Override
    public void update(Goods goods) {


        if(goods.getSellPrice()!=null && goods.getSellPrice()==0){
            goods.setSellPrice(null);
        }
        if(goods.getSellPrice()!=null && goods.getSellPrice()<=0){
            throw new MsgException("价格不能小于等于0");
        }
        goodsDao.updateByPrimaryKey(goods);
    }

    @Transactional
    @Override
    public void buy(Goods goods, long userId) {

        Goods goods0 = goodsDao.selectByPrimaryKey(goods.getId());

        if(goods.getNum()<=0){
            throw new MsgException();
        }

        if(goods0.getSellPrice()==null){
            throw new MsgException("该商品不卖");
        }
        if((double)goods.getSellPrice() != (double)goods0.getSellPrice()){
            throw new MsgException("该商品价格发生变化");
        }

        if(goods.getNum()>goods0.getNum()){
            throw new MsgException("数量不足");
        }


        //结账

        double money = goods.getNum()*goods.getSellPrice();

        transferService.transfer(userId,goods0.getOwnerUserId(),money,"购买"+goods0.getName(),null);


        double oldNum = goods0.getNum()-goods.getNum();

        if(oldNum==0){
            goodsDao.deleteByPrimaryKey(goods0.getId());
        }else{
            goods0.setNum(oldNum);
            goodsDao.updateNum(goods0);
        }

        goods0.setNum(goods.getNum());
        goods0.setOwnerUserId(userId);
        goodsDao.insert(goods0);













    }

}
