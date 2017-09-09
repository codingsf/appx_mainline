package top.appx.factory;

import top.appx.entity.Goods;

public class GoodsFactory {
    public static Goods createSlave(long ownerUserId,long user1Id){
        Goods goods = new Goods();
        goods.setName("奴隶契约");
        goods.setFlag("slave_contract");
        goods.setDescription("拥有该奴隶契约的人在该奴隶签到时候可以获得5积分奖励");
        goods.setNum(1);
        goods.setOwnerUserId(ownerUserId);
        goods.setUser1Id(user1Id);
        return goods;
    }
}
