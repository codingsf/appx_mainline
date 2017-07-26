package top.appx.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import top.appx.entity.Notify;

@Component
@Mapper
public interface NotifyDao extends BaseDao<Notify> {

}
