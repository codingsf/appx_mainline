package top.appx.entity.vo;

import top.appx.entity.BaseEntity;
import top.appx.entity.Goods;

public class GoodsVO extends Goods {
    private String ownerUserNickname;
    private String user1Nickname;

    public String getOwnerUserNickname() {
        return ownerUserNickname;
    }

    public void setOwnerUserNickname(String ownerUserNickname) {
        this.ownerUserNickname = ownerUserNickname;
    }

    public String getUser1Nickname() {
        return user1Nickname;
    }

    public void setUser1Nickname(String user1Nickname) {
        this.user1Nickname = user1Nickname;
    }
}
