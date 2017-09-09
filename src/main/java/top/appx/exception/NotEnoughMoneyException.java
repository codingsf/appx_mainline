package top.appx.exception;

public class NotEnoughMoneyException extends MsgException {
    public NotEnoughMoneyException(){
        super("积分不够");
    }
}
