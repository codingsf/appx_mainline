package top.appx.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.appx.entity.Permission;
import top.appx.dao.PermissionDao;
import top.appx.service.PermissionService;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionDao permissionDao;


    @Override
    public List<Permission> findListPermissionByUserId(Long id) {
        return permissionDao.findListPermissionByUserId(id);
    }
    @Override
    public List<Permission> find() {
        return permissionDao.find();
    }
}
