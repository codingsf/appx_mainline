package top.appx.config.cache;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * @author cuiP
 * Created by JK on 2017/3/13.
 */
@Configuration
@EnableCaching//标注启动缓存.
public class CacheConfiguration {
    /*
      * 据shared与否的设置,
      * Spring分别通过CacheManager.create()
      * 或new CacheManager()方式来创建一个ehcache基地.
      *
      * 也说是说通过这个来设置cache的基地是这里的Spring独用,还是跟别的(如hibernate的Ehcache共享)
      *
      */
    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean(){
        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean ();
        cacheManagerFactoryBean.setConfigLocation (new ClassPathResource("ehcache-shiro.xml"));
        cacheManagerFactoryBean.setShared(true);
        System.out.println("ttttt");
        return cacheManagerFactoryBean;
    }

    /**
     *  ehcache 主要的管理器
     * @param ehCacheManagerFactoryBean
     * @return
     */
    @Bean
    public EhCacheCacheManager ehCacheCacheManager(EhCacheManagerFactoryBean ehCacheManagerFactoryBean){
        return new EhCacheCacheManager(ehCacheManagerFactoryBean.getObject());
    }


}
