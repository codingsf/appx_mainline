package top.appx.service;

import com.github.pagehelper.PageInfo;
import top.appx.entity.Permission;
import top.appx.entity.User;

import java.util.List;

public interface PermissionService {
    List<Permission> findListPermissionByUserId(Long id);
    List<Permission> find();
}
