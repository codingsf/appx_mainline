package top.appx.exception;

import org.springframework.http.HttpStatus;

public class EmailExistException extends MsgException {
    public EmailExistException() {
        super(HttpStatus.BAD_REQUEST,"邮箱已经已经存在!");
    }
}
