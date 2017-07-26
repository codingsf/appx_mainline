package top.appx.exception;

import org.springframework.http.HttpStatus;

public class ArticleGroupNameExistException extends MsgException {
    public ArticleGroupNameExistException(){
        super(HttpStatus.BAD_REQUEST,"名称已经存在!");
    }
}
