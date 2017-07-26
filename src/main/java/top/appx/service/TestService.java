package top.appx.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    @Cacheable(value = "ttttttttttt",key = "aaaaa")
    public String getStr(){
        System.out.println("invoke getStr");
        return "aaa";
    }
}
