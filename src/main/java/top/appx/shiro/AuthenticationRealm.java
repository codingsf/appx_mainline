package top.appx.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import top.appx.dao.PermissionDao;
import top.appx.dao.RoleDao;
import top.appx.dao.UserDao;
import top.appx.entity.Permission;
import top.appx.entity.Role;
import top.appx.entity.User;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationRealm extends AuthorizingRealm{

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserDao userMapper;

    @Autowired
    private RoleDao roleMapper;

    @Autowired
    private PermissionDao permissionMapper;

    /**
     * 认证 校验用户身份是否合法
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        logger.debug("##################执行Shiro权限认证##################");
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

        User record = null;
        record = userMapper.findByUsername(token.getUsername());

        if(record == null){
            throw new UnknownAccountException();// 没找到帐号
        }
        if (Boolean.TRUE.equals(record.getLock())) {
            throw new LockedAccountException(); // 帐号锁定
        }

        Role role = roleMapper.findByUserId(record.getId());




        //设置角色名称
        record.setRoleName(role.getName());


        //将此用户存放到登录认证info中，无需自己做密码对比，Shiro使用CredentialsMatcher会为我们进行密码对比校验
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                record, record.getPassword(), getName());
//        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
//                record, // 用户名
//                record.getPassword(), // 密码
//                ByteSource.Util.bytes(record.getSalt()),// salt=username+salt
//                getName() // realm name
//        );

        return authenticationInfo;
    }
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        logger.debug("=========执行授权===========");
        //获取主身份信息
        User user = (User) principalCollection.getPrimaryPrincipal();

        List<Permission> permissionList = permissionMapper.findListPermissionByUserId(user.getId());

        //单独定一个集合对象
        List<String> permissions = new ArrayList<String>();
        if(permissionList!=null){
            for(Permission permission:permissionList){
                //将数据库中的权限标签 符放入集合
                permissions.add(permission.getPerms());
            }
        }
        logger.debug("授权:"+permissions.toArray());

        //查到权限数据，返回授权信息(要包括 上边的permissions)
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //将上边查询到授权信息填充到simpleAuthorizationInfo对象中
        simpleAuthorizationInfo.addStringPermissions(permissions);
        simpleAuthorizationInfo.addRole(user.getRoleName());
        return simpleAuthorizationInfo;
    }



}
