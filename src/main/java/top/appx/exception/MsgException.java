package top.appx.exception;

import org.springframework.http.HttpStatus;

public class MsgException extends RuntimeException {
    private HttpStatus httpStatus;

    public MsgException(){
        this("系统错误");
    }

    public MsgException(String msg){
        super(msg);
        httpStatus = HttpStatus.BAD_REQUEST;
    }
    public MsgException(HttpStatus httpStatus){
        this.httpStatus = httpStatus;
    }

    public MsgException(HttpStatus httpStatus,String message){
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

}
