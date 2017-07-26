package top.appx.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import top.appx.entity.Permission;

import java.util.List;
@Component
@Mapper
public interface PermissionDao extends BaseDao<Permission> {
    List<Permission> findListPermissionByUserId(Long id);
}
