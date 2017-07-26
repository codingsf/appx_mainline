package top.appx.exception;

import org.springframework.http.HttpStatus;

public class PhoneExistException extends MsgException {
    public PhoneExistException(){
        super(HttpStatus.BAD_REQUEST,"手机号已经存在!");
    }
}
