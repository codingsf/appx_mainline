package top.appx.service;

import top.appx.entity.Permission;

import java.util.List;

public interface PermissionService {
    List<Permission> findListPermissionByUserId(Long id);
    List<Permission> find();
}
