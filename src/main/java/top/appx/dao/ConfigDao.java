package top.appx.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import top.appx.entity.Config;

@Component
@Mapper
public interface ConfigDao extends BaseDao<Config>  {
}
