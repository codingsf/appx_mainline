package top.appx.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import top.appx.entity.User;

import java.util.List;

@Component
@Mapper
public interface UserDao extends BaseDao<User> {
    User findByUsername(String username);
    User findByPhone(String phone);
    User findByEmail(String email);
    List<User> find(User user);
    List<User> findSubscribeUser(long articleGroupId);

    void createIcard(User user);

    User findByIcard(String icard);

    List<User> findManager();


    User findByQQOpenId(String qqOpenId);

    void updateQQOpenId(User user);
}
