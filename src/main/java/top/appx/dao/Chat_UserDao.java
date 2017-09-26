package top.appx.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import top.appx.entity.Chat_User;

@Component
@Mapper
public interface Chat_UserDao extends BaseDao<Chat_User> {
    void insert(Chat_User chat_user);
}
