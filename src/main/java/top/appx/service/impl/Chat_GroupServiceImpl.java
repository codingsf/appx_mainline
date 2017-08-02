package top.appx.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.appx.dao.Chat_GroupDao;
import top.appx.entity.Chat_Group;
import top.appx.entity.User;
import top.appx.service.Chat_GroupService;

import java.util.List;

@Service
public class Chat_GroupServiceImpl implements Chat_GroupService {
    @Autowired
    private Chat_GroupDao chat_groupDao;

    @Override
    public List<Chat_Group> find(){
        return chat_groupDao.find();
    }

}
