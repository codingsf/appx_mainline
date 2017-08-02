package top.appx.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import top.appx.entity.Chat_Group;

@Component
@Mapper
public interface Chat_GroupDao extends BaseDao<Chat_Group>{

}