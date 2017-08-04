package top.appx.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.appx.dao.RoleDao;
import top.appx.dao.UserDao;
import top.appx.dao.UserRoleDao;
import top.appx.entity.Article;
import top.appx.entity.Role;
import top.appx.entity.User;
import top.appx.entity.UserRole;
import top.appx.exception.EmailExistException;
import top.appx.exception.MsgException;
import top.appx.exception.PhoneExistException;
import top.appx.exception.UsernameExistException;
import top.appx.service.UserService;
import top.appx.util.PasswordUtil;
import top.appx.util.StringUtil;

import java.util.Date;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserRoleDao userRoleDao;

    @Transactional(readOnly=true)
    @Override
    public PageInfo<User> findPage(Integer pageNum ,Integer pageSize) throws Exception {
        PageHelper.startPage(pageNum,pageSize);
        User user = new User();
        List<User> userList = userDao.find(user);
        return new PageInfo<User>(userList);
    }

    @Transactional
    @Override
    public void saveUserAndUserRole(User user, Long roleId) {
        Date now = new Date();

        if(userDao.findByUsername(user.getUsername())!=null){
            throw new UsernameExistException();
        }

        if(!StringUtil.isNullOrEmpty(user.getPhone())){
            if(userDao.findByPhone(user.getPhone())!=null){
                throw new PhoneExistException();
            }
        }

        if(userDao.findByEmail(user.getEmail())!=null){
            throw new EmailExistException();
        }

        if(StringUtil.isNullOrEmpty(user.getAvatar())){
            user.setAvatar("http://tva1.sinaimg.cn/crop.219.144.555.555.180/0068iARejw8esk724mra6j30rs0rstap.jpg");
        }


        try {
            user.setPassword(PasswordUtil.md532(user.getPassword()));
        }catch (Exception ex){
            throw new MsgException(HttpStatus.INTERNAL_SERVER_ERROR,"系统错误!!!!!");
        }

        user.setLock(false);
        user.setCreateTime(now);

      //  user.setStatus(1);
        Role role =roleDao.findById(roleId);

        userDao.insert(user);

        UserRole userRole = new UserRole();
        userRole.setRoleId(role.getId());
        userRole.setUserId(user.getId());
        userRole.setCreateTime(now);
        userRole.setModifyTime(now);

        userRoleDao.insert(userRole);

    }

    @Transactional
    @Override
    public void register(User user){
        saveUserAndUserRole(user,2L);
    }

    @Cacheable(value="test_num")
    public Article getNum() {
        Article article = new Article();
        article.setCreateTime(new Date());
        return article;

    }

    @Override
    public void test() {
        System.out.println("get"+getNum().getCreateTime());
    }

    @Override
    public String getStr(){
        System.out.println("getStr inv");
        return "tttt";
    }

    @Override
    public List<User> findSubscribeUser(long articleGroupId) {
        return userDao.findSubscribeUser(articleGroupId);
    }

    @Override
    public void update(User user) {
        userDao.updateByPrimaryKey(user);
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        userDao.deleteByPrimaryKeys(ids);
    }

    @Override
    public String createIcard(User user) {
        userDao.createIcard(user);
        return userDao.selectByPrimaryKey(user.getId()).getIcard();
    }

    @Override
    public User findByIcard(String icard) {
        if(icard==null){return null;}
        return userDao.findByIcard(icard);
    }

    @Override
    public List<User> findManager() {
        return userDao.findManager();
    }

    @Override
    public boolean emailExist(String email) {
        return userDao.findByEmail(email)!=null;
    }


}
