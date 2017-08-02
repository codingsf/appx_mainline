package top.appx.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@Configuration
@ConfigurationProperties(prefix = "appx")
public class AppxConfig {
    private String domain;
    private boolean scheduleInit;

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
}
