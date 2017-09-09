package top.appx.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.appx.exception.EmailExistException;
import top.appx.exception.MsgException;
import top.appx.exception.PhoneExistException;
import top.appx.exception.UsernameExistException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 异常拦截
 */
@Aspect
@Component
public class ExceptionAspect {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Pointcut("execution(public * top.appx.controller.*.*(..) ) ")
    public void controllerPointcut(){

    }

/*    @AfterReturning(pointcut = "controllerPointcut()",returning = "object")
    public void rt(JoinPoint joinPoint, Object object) throws IOException {

        Class returnType = ((MethodSignature)joinPoint.getSignature()).getReturnType();
        if(returnType == void.class){
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletResponse response = attributes.getResponse();
            response.getWriter().write("{\"success\":true}");
        }

    }*/



    @AfterThrowing(pointcut = "controllerPointcut()",throwing = "ex")
    public void doOp(Throwable ex){
        logger.info("拦截到异常信息 {0}",ex);
        if(ex instanceof MsgException){
            writeResponse(ResponseEntity.status(((MsgException) ex).getHttpStatus()).body(ex.getMessage()));
        }else{
            logger.error("未能识别的异常:{}",ex);
            writeResponse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
        }
    }
    private void writeResponse(ResponseEntity responseEntity){
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        if(responseEntity!=null) {
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setStatus(responseEntity.getStatusCode().value());

            if(responseEntity.getBody()!=null) {
                PrintWriter writer = null;
                try {
                    writer = response.getWriter();
                } catch (IOException e) {
                    logger.info("getWriter异常:" + e.getMessage());
                }

                writer.print(responseEntity.getBody().toString());
                writer.flush();
                writer.close();
            }
        }

    }

}
