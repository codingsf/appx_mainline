package top.appx.exception;

import org.springframework.http.HttpStatus;

public class NameExistException extends MsgException {
    public NameExistException(){
        super(HttpStatus.BAD_REQUEST,"名称已经存在!");
    }
}

