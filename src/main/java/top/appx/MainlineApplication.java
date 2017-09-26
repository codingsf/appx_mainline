package top.appx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import top.appx.zutil.HttpUtil;

import java.net.URLEncoder;

@SpringBootApplication
//@EnableScheduling
@EnableWebSocket
@EnableCaching
//@EnableRedisHttpSession
public class MainlineApplication extends SpringBootServletInitializer {


	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(MainlineApplication .class);
	}

	public static void main(String[] args) {
		try {

			String str = HttpUtil.httpPost("http://news.appx.top:5700/send_private_msg", "user_id=799378666&message="+URLEncoder.encode("程序运行","utf-8") );
			System.out.println(str);
		} catch (Exception e) {
			e.printStackTrace();
		}

		SpringApplication.run(MainlineApplication.class, args);
	}

	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("messages");
		return messageSource;
	}

}
