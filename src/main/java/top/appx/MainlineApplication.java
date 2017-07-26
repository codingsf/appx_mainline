package top.appx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableScheduling
//@EnableCaching
//@EnableRedisHttpSession
public class MainlineApplication {
	public static void main(String[] args) {
		System.out.println("tttt");
		SpringApplication.run(MainlineApplication.class, args);
	}
}
