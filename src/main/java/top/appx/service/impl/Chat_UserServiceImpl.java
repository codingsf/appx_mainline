package top.appx.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.appx.entity.Chat_User;
import top.appx.dao.Chat_UserDao;
import top.appx.service.Chat_UserService;

import java.util.Date;

@Service
public class Chat_UserServiceImpl implements Chat_UserService {
    @Autowired
    private Chat_UserDao chat_userDao;

    @Override
    public void insert(Chat_User chat_user) {
        chat_user.setCreateTime(new Date());
        chat_userDao.insert(chat_user);
    }
}
