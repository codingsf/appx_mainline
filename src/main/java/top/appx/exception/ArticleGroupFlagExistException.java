package top.appx.exception;

import org.springframework.http.HttpStatus;

public class ArticleGroupFlagExistException extends MsgException {
    public ArticleGroupFlagExistException() {
        super(HttpStatus.BAD_REQUEST,"标识已经存在!");
    }
}
