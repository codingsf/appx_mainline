package top.appx.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import top.appx.entity.Cookie;

@Component
@Mapper
public interface CookieDao extends BaseDao<Cookie> {
    Cookie findUse(String flag);

    void updateUsed(Cookie cookie);
}
