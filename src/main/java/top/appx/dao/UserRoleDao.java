package top.appx.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import top.appx.entity.UserRole;
@Component
@Mapper
public interface UserRoleDao extends BaseDao<UserRole> {

}
