package top.appx.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.text.SimpleDateFormat;

@Configuration
public class DateConvertConfig {

    @Bean
    public MappingJackson2HttpMessageConverter getBean(){
        MappingJackson2HttpMessageConverter mmc = new MappingJackson2HttpMessageConverter();
        ObjectMapper om = new ObjectMapper();
        om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        mmc.setObjectMapper(om);
        return mmc;
    }
}
