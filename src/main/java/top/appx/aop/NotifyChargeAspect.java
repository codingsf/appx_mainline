package top.appx.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.appx.entity.Goods;
import top.appx.entity.Notify;
import top.appx.entity.User;
import top.appx.entity.vo.GoodsVO;
import top.appx.exception.NotEnoughMoneyException;
import top.appx.factory.GoodsFactory;
import top.appx.service.GoodsService;
import top.appx.service.TransferService;
import top.appx.service.UserService;

import java.util.List;

@Aspect
@Component
public class NotifyChargeAspect {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TransferService transferService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private UserService userService;
 /*   @Before(value = "execution(public * top.appx.service.impl.NotifyServiceImpl.save(..))")
    public void t1(JoinPoint joinPoint)throws Exception{
        Notify notify = (Notify)joinPoint.getArgs()[0];
        if(notify.getTargetUserId()!=null && notify.getTargetUserId()!=1){
            User user = userService.findById(notify.getTargetUserId());
            if(user.getMoney()<=0){
                throw new NotEnoughMoneyException();
            }
        }
    }*/

/*

    @AfterReturning(value = "execution(public * top.appx.service.impl.NotifyServiceImpl.save(..))",returning = "retValue")
    public void t(JoinPoint joinPoint,Object retValue)throws Exception{
            Notify notify = (Notify)joinPoint.getArgs()[0];
            if(notify.getTargetUserId()!=null && notify.getTargetUserId()!=1 && (boolean)retValue){
                transferService.transfer(notify.getTargetUserId(),1L,1,"消息提醒转账("+notify.getType()+")",null);
            }
    }
*/

    @After(value = "execution(public * top.appx.service.impl.UserServiceImpl.register(..))")
    public void t2(JoinPoint joinPoint){
        try {
            User user = (User)joinPoint.getArgs()[0];
            if(user.getInviteUserId()!=null){
                if(userService.findById(user.getInviteUserId())!=null){
                    //生成一张奴隶契约
                    Goods goods  = GoodsFactory.createSlave(user.getInviteUserId(),user.getId());
                    goodsService.save(goods);

                    transferService.transfer(1L,user.getInviteUserId(),100,"邀请好友奖励",null);
                }
            }
        }catch (Exception ex){
            logger.error("邀请奖励出错",ex);
        }
    }

    @After(value="execution(public * top.appx.service.impl.UserServiceImpl.sign(..))")
    public void t3(JoinPoint joinPoint){
        try {
            long userId = (long)joinPoint.getArgs()[0];
            User user = userService.findById(userId);
            if(user!=null){
                transferService.transfer(1L,user.getId(),20,"签到",null);

                //获取该用户主人账号
                Goods search = new Goods();
                search.setUser1Id(user.getId());
                List<GoodsVO> slaves = goodsService.findVO(search);
                //slaves数量只能是1
                for (GoodsVO slave : slaves) {
                    if(slave.getOwnerUserId()!=1L) {
                        transferService.transfer(1L, slave.getOwnerUserId(), 5, "奴隶契约<" + slave.getUser1Nickname() + ">签到奖励", "道具卡作用");
                    }
                }
            }
        }catch (Exception ex){
            logger.error("签到奖励出错",ex);
        }

    }


}
