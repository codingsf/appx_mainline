package top.appx.exception;

/**
 * 一般异常,程序很有可能发生的异常
 */
public class NormalException extends Exception {
    public NormalException(String msg){
        super(msg);
    }
}
