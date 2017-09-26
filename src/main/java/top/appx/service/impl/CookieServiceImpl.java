package top.appx.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.appx.dao.CookieDao;
import top.appx.entity.Cookie;
import top.appx.service.CookieService;

import java.util.Date;
import java.util.List;

@Service
public class CookieServiceImpl implements CookieService {

    @Autowired
    private CookieDao cookieDao;
    @Override
    public PageInfo<Cookie> findPage(Integer pageNum, Integer pageSize) throws Exception {
        PageHelper.startPage(pageNum,pageSize);
        List<Cookie> list = cookieDao.find();
        return new PageInfo<Cookie>(list);
    }

    @Override
    public void save(Cookie cookie) {
        cookie.setCreateTime(new Date());
        cookieDao.insert(cookie);
    }

    @Override
    public void update(Cookie cookie) {
        cookie.setModifyTime(new Date());
        cookieDao.updateByPrimaryKey(cookie);
    }

    @Override
    public void del(List<Long> ids) {
        cookieDao.deleteByPrimaryKeys(ids);
    }

    @Override
    public Cookie findAndMrak(String flag) {
        Cookie cookie = cookieDao.findUse(flag);
        cookie.setLastUseTime(new Date());
        cookieDao.updateUsed(cookie);

        return cookie;
    }
}
