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

    String createIcard(User user);

    User findByIcard(String icard);

    List<User> findManager();//获取管理员

    boolean emailExist(String email);

    User findByQQOpenId(String openId);

    void registerByQQOld(User userEntity);

    void inviteAward(Long userId);

    User findById(Long id);

    List<User> findByInviteUserId(Long inviteUserId);

    void sign(Long id);

    /**
     * 每天零点重置方法
     */
    void resetByAnyDay0();

    List<User> moneyTop10();

    /**
     * 更新提醒设置
     * @param user
     */

    void updateNotifySz(User user);
}
