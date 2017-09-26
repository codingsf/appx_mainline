package top.appx.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.appx.dao.RoleDao;
import top.appx.entity.Article;
import top.appx.exception.*;
import top.appx.dao.UserDao;
import top.appx.dao.UserRoleDao;
import top.appx.entity.Role;
import top.appx.entity.User;
import top.appx.entity.UserRole;
import top.appx.exception.*;
import top.appx.service.UserService;
import top.appx.zutil.StringUtil;
import top.appx.zutil.PasswordUtil;

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

        if(user.getUsername().contains("@")){
            throw new MsgException("用户名中不能包含@符号");
        }

        if(userDao.findByUsername(user.getUsername())!=null){
            throw new UsernameExistException();
        }




        if(!StringUtil.isNullOrEmpty(user.getPhone())){
            if(userDao.findByPhone(user.getPhone())!=null){
                throw new PhoneExistException();
            }
        }

        if(!StringUtil.isNullOrEmpty(user.getQq())){
            if(userDao.findByQq(user.getQq())!=null){
                throw new QQExistException();
            }
        }


        if(userDao.findByEmail(user.getEmail())!=null){
            throw new EmailExistException();
        }



        if(!StringUtil.isNullOrEmpty(user.getQqOpenId()) && userDao.findByQQOpenId(user.getQqOpenId())!=null){
            throw new RuntimeException();
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
    @Transactional
    @Override
    public void registerByQQOld(User userEntity) {
        if(userDao.findByQQOpenId(userEntity.getQqOpenId())!=null){
            throw new RuntimeException();
        }

        User user = userDao.findByUsername(userEntity.getUsername());
        if(user==null){
            throw new MsgException(HttpStatus.BAD_REQUEST,"用户名不存在");
        }

        String md5pwd = null;
        try {
            md5pwd = PasswordUtil.md532(userEntity.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!user.getPassword().equals(md5pwd)){
            throw new MsgException(HttpStatus.BAD_REQUEST,"密码错误");
        }
        user.setQqOpenId(userEntity.getQqOpenId());
        userDao.updateQQOpenId(user);

    }

    @Override
    public void inviteAward(Long userId) {


    }

    @Override
    public User findById(Long id) {
       return userDao.selectByPrimaryKey(id);
    }

    @Override
    public List<User> findByInviteUserId(Long inviteUserId) {
        return userDao.findByInviteUserId(inviteUserId);
    }

    @Override
    public void sign(Long id) {
        userDao.sign(id,new Date());
    }

    @Override
    public void resetByAnyDay0() {
        userDao.resetByAnyDay0();
    }


    @Override
    public List<User> moneyTop10() {
        return userDao.moneyTop10();
    }

    @Override
    public void updateNotifySz(User user) {
        userDao.updateNotifySz(user);
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

    @Transactional
    @Override
    public void update(User user) {
        User tmp = null;

        if(StringUtil.isNullOrEmpty(user.getQq())){
            tmp = userDao.findByQq(user.getQq());
            if(tmp!=null && tmp.getId()!=user.getId()){
                throw new MsgException("qq号已经被占用");
            }
        }

        tmp = userDao.findByEmail(user.getEmail());
        if(tmp!=null && tmp.getId()!=user.getId()){
            throw new MsgException("邮箱已经被占用");
        }
        if(!StringUtil.isNullOrEmpty(user.getPhone())){
            tmp = userDao.findByPhone(user.getPhone());
            if(tmp!=null && tmp.getId()!=user.getId()){
                throw new MsgException("手机号不能为空");
            }
        }



        user.setEmailNotify(false);
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

    @Override
    public User findByQQOpenId(String openId) {
        User user1 =   userDao.findByQQOpenId(openId);
        return user1;
    }




}
