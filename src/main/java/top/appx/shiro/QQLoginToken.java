package top.appx.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

public class QQLoginToken extends UsernamePasswordToken {
    public QQLoginToken(String username,String password,Boolean rememberMe){
        super(username,password,rememberMe);
    }
}
