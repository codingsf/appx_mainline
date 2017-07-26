package top.appx.aop;

import org.aspectj.lang.Aspects;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ReturnAspect{
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Pointcut("execution(public * top.appx.controller.*.*(..) ) ")
    public void controllerPointcut(){
    }

  /*  @AfterReturning(returning = "ret",pointcut = "controllerPointcut()")
    public void doAfterReturning(Object ret) throws Throwable {
        System.out.println("ret is "+ret);

    }*/


}
