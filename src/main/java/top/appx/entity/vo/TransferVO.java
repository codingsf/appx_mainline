package top.appx.entity.vo;

import top.appx.entity.Transfer;

public class TransferVO extends Transfer {
    private String sUserNickname;
    private String dUserNickname;

    public String getsUserNickname() {
        return sUserNickname;
    }

    public void setsUserNickname(String sUserNickname) {
        this.sUserNickname = sUserNickname;
    }

    public String getdUserNickname() {
        return dUserNickname;
    }

    public void setdUserNickname(String dUserNickname) {
        this.dUserNickname = dUserNickname;
    }
}
