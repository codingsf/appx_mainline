package top.appx.service;

import com.github.pagehelper.PageInfo;
import top.appx.entity.Cookie;

import java.util.List;


public interface CookieService {
    PageInfo<Cookie> findPage(Integer pageNum , Integer pageSize) throws Exception;

    void save(Cookie cookieStr);

    void update(Cookie cookieStr);

    void del(List<Long> ids);


    Cookie findAndMrak(String flag);
}
