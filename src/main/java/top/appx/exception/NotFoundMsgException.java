package top.appx.exception;

import org.springframework.http.HttpStatus;

public class NotFoundMsgException extends MsgException {
    public NotFoundMsgException() {
        super(HttpStatus.NOT_FOUND,"404");
    }
}
