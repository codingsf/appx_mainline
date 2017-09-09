package top.appx.exception;

public class QQExistException extends MsgException {
    public QQExistException(){
        super("QQ已经存在");
    }
}
