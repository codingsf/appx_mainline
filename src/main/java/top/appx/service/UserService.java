package top.appx.service;

import com.github.pagehelper.PageInfo;
import org.springframework.cache.annotation.Cacheable;
import top.appx.entity.User;

import java.util.List;

public interface UserService {

    /**
     *
     * @param pageNum  当前页码
     * @param pageSize  每页显示条数
     * @return
     * @throws Exception
     */
    PageInfo<User> findPage(Integer pageNum , Integer pageSize) throws Exception;

    void saveUserAndUserRole(User user,Long roleId)throws Exception;

    void register(User user);

    void test();
    @Cacheable(value = "ttaaaasdf")
    String getStr();


    List<User> findSubscribeUser(long articleGroupId);

    void update(User user);


    void deleteByIds(List<Long> ids);
}
