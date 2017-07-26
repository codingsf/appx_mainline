package top.appx.exception;

import org.springframework.http.HttpStatus;

/**
 * 用户名已经存在异常
 */
public class UsernameExistException extends MsgException {
    public UsernameExistException(){
        super(HttpStatus.BAD_REQUEST,"用户名已经存在");
    }
}
