package top.appx.config.task;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestTemplate;
import top.appx.config.ApplicationContextStatic;
import top.appx.dao.BaseDao;
import top.appx.dao.CollectParamDao;
import top.appx.entity.CollectParam;
import top.appx.entity.ScheduleJob;
import top.appx.entity.vo.QrtzJob;
import top.appx.job.CollectJob;
import top.appx.job.CollectJob_Twitter;
import top.appx.job.CollectJob_Weichat;
import top.appx.service.ArticleService;
import top.appx.util.StringUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * 调度任务执行
 * Created by cuiP on 2017/5/20.
 */
public class ScheduleExecute {

    protected static final Logger log = LoggerFactory.getLogger(ScheduleExecute.class);
    private static CollectParamDao collectParamDao;

    public static void execute(ApplicationContext applicationContext, QrtzJob qrtzJob){


        long startTime = System.currentTimeMillis();
        try {
            ScheduleExecute.invokMethod(qrtzJob,applicationContext);
        } catch (Exception e) {
            //任务执行总时长
            long times = System.currentTimeMillis() - startTime;

            log.error("调度任务执行失败",e);
        }finally {
        }
    }



    public static Map<String,Object> objMap = new Hashtable<>();
    /**
     * 通过反射调用scheduleJob中定义的方法
     *
     * @param qrtzJob
     */
    public static void invokMethod(QrtzJob qrtzJob,ApplicationContext applicationContext) {


        Object object = null;
        Class<?> clazz = null;

        if("scheduleJob".equals(qrtzJob.getType())){
            //#region schduleJob任务处理
            ScheduleJob scheduleJob = qrtzJob.getScheduleJob();
            if(objMap.containsKey(scheduleJob.getBeanClass())){
                object = objMap.get(scheduleJob.getBeanClass());
            }
            else if (StringUtils.isNotBlank(scheduleJob.getBeanClass())) {
                try {
                    clazz = Class.forName(scheduleJob.getBeanClass());
                    try {

                        object = applicationContext.getBean(clazz);
                    }catch (Exception e0){
                        System.out.println("错误:"+e0.getMessage());
                    }

                    if(object==null){
                        object = clazz.newInstance();
                    }
                    objMap.put(scheduleJob.getBeanClass(),object);
                } catch (Exception e) {
                    log.error("任务名称 = [" + scheduleJob.getName()
                            + "]---出错,"+e.getMessage());
                }

                if (object == null) {
                    log.error("任务名称 = [" + qrtzJob.getName()
                            + "]---------------未启动成功，请检查是否配置正确！！！");
                    return;
                }
                clazz = object.getClass();
                Method method = null;
                try {
                    if(StringUtil.isNullOrEmpty(scheduleJob.getParams())){
                        method = clazz.getMethod(scheduleJob.getMethodName());
                    }else{
                        method = clazz.getDeclaredMethod(scheduleJob.getMethodName(), String.class);
                    }

                } catch (NoSuchMethodException e) {
                    log.error("任务名称 = [" + scheduleJob.getName()
                            + "]---------------未启动成功，方法名设置错误！！！");
                } catch (SecurityException e) {
                    log.error(e.getMessage(), e);
                }


                if (method != null) {
                    try {
                        ReflectionUtils.makeAccessible(method);
                        if(StringUtils.isNotBlank(scheduleJob.getParams())){
                            method.invoke(object, scheduleJob.getParams());
                        }else{
                            method.invoke(object);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        log.error("任务名称 = [" + scheduleJob.getName() + "]----------执行出错 "+e.getTargetException().getMessage());
                    }
                }

            }
            //#endregion schduleJob任务处理
        }

        else if("collectParam".equals(qrtzJob.getType())){
            //#region collectParam任务处理
            CollectParam collectParam = qrtzJob.getCollectParam();
            String keyName = "collectParam_"+collectParam.getId();
            CollectJob collectJob = null;
            if(objMap.containsKey(keyName)){
                collectJob = (CollectJob)objMap.get(keyName);
            }
            else{
                if("twitter".equals(collectParam.getType())){
                    collectJob = new CollectJob_Twitter(collectParam,applicationContext);
                }
                else if("wechat".equals(collectParam.getType())){
                    collectJob = new CollectJob(collectParam,applicationContext);
                }
                else{
                    collectJob = new CollectJob(collectParam,applicationContext);
                }
                objMap.put(keyName,collectJob);
            }

            if(collectParamDao==null){
                collectParamDao = applicationContext.getBean(CollectParamDao.class);
            }
            try {
                collectJob.execute();
                System.out.println("collectParam 任务处理");

                collectParam.setLastSuccessTime(new Date());
                collectParamDao.updateLastSuccess(collectParam);


            }catch (Exception ex){
                collectParam.setLastErrorTime(new Date());
                log.error(qrtzJob.getName()+"执行出错",ex);
                collectParam.setErrorMsg(ex.getMessage());
                collectParamDao.updateLastError(collectParam);

            }

            //#endregion collectParam任务处理
        }








     //   log.debug("任务名称 = [" + scheduleJob.getName() + "]----------启动成功");
    }
}
