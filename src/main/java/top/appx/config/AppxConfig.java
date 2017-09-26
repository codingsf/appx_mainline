package top.appx.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@Configuration
@ConfigurationProperties(prefix = "appx")
public class AppxConfig {
    private String domain;
    private boolean scheduleInit;
    private String proxy_host;
    private Integer proxy_port;
    private String qqgroupids;
    private String qqrobot;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }


    public boolean isScheduleInit() {
        return scheduleInit;
    }

    public void setScheduleInit(boolean scheduleInit) {
        this.scheduleInit = scheduleInit;
    }

    public String getProxy_host() {
        return proxy_host;
    }

    public void setProxy_host(String proxy_host) {
        this.proxy_host = proxy_host;
    }


    public Integer getProxy_port() {
        return proxy_port;
    }

    public void setProxy_port(Integer proxy_port) {
        this.proxy_port = proxy_port;
    }

    public String getQqgroupids() {
        return qqgroupids;
    }

    public void setQqgroupids(String qqgroupids) {
        this.qqgroupids = qqgroupids;
    }

    public String getQqrobot() {
        return qqrobot;
    }

    public void setQqrobot(String qqrobot) {
        this.qqrobot = qqrobot;
    }
}
