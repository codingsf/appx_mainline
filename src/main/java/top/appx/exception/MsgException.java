package top.appx.exception;

import org.springframework.http.HttpStatus;

public class MsgException extends RuntimeException {
    private HttpStatus httpStatus;

    public MsgException(){

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
