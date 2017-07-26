package top.appx.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import top.appx.entity.Role;
@Component
@Mapper
public interface RoleDao extends BaseDao<Role> {
    Role findByUserId(Long id);
    Role findById(Long id);
}
