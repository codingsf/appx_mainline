package top.appx.controller;

import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import top.appx.controller.BaseController;
import top.appx.entity.User;
import top.appx.exception.EmailExistException;
import top.appx.exception.MsgException;
import top.appx.exception.PhoneExistException;
import top.appx.exception.UsernameExistException;
import top.appx.service.MailService;
import top.appx.service.UserService;
import top.appx.util.ResponseMap;
import top.appx.util.StringUtil;

import javax.servlet.http.HttpSession;
import javax.xml.ws.Response;
import java.util.HashMap;

@Controller
public class LoginController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @GetMapping("/login")
    public Object login(){
        return "/login";
    }

    @GetMapping("/register")
    public String register(){
        return "/register";
    }

    @GetMapping("/user/auth")
    @ResponseBody
    public void auth(@RequestParam("mail") String mail, HttpSession session)throws Exception{
        String checkCode =(int)((Math.random()*9+1)*1000)+"";
        mailService.sendSimpleMail(mail,"您的验证码为:"+checkCode,"你的验证码为:"+checkCode);
        session.setAttribute("checkCode_"+mail,checkCode);
    }
    @PostMapping("/user/register")
    @ResponseBody
    public Object register(User userEntity,String checkCode,HttpSession session){

        System.out.println("注册中");
        if(StringUtil.isNullOrEmpty(userEntity.getEmail())){
            throw new MsgException(HttpStatus.BAD_REQUEST,"邮箱不能为空");
        }
        Object checkCodeObj = session.getAttribute("checkCode_"+userEntity.getEmail());
        if(checkCodeObj==null){
            throw new MsgException(HttpStatus.BAD_REQUEST,"没有生成验证码或验证码已经失效");
        }

        session.setAttribute("checkCode_"+userEntity.getEmail(),null);
        if(checkCodeObj!=null && checkCodeObj.toString().equals(checkCode)){

        }else{
            throw new MsgException(HttpStatus.BAD_REQUEST,"验证码错误");
        }
        if(StringUtil.isNullOrEmpty(userEntity.getUsername())){
            throw new MsgException(HttpStatus.BAD_REQUEST,"用户名不能为空");
        }
        if(StringUtil.isNullOrEmpty(userEntity.getPassword())){
            throw new MsgException(HttpStatus.BAD_REQUEST,"密码不能为空");
        }

        userService.register(userEntity);
        HashMap<String,Object> hashMap = new HashMap<String,Object>();
        hashMap.put("success",true);

        try{
            mailService.sendSimpleMail("799378666@qq.com","新用户注册提醒","新用户注册,邮箱:"+userEntity.getEmail());
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return hashMap;
    }




    @PostMapping("/login")
    @ResponseBody
    public Object login(String username,String password){
        try {
            //获取当前的Subject
            Subject currentUser = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(username, password,true);
            //在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
            //每个Realm都能在必要时对提交的AuthenticationTokens作出反应
            //所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
            logger.info("对用户进行登录验证..验证开始! username = {}", username);
            currentUser.login(token);
            //验证是否登录成功
            if(currentUser.isAuthenticated()){
                logger.info("对用户进行登录验证..验证通过! username = {}", username);
                return ResponseEntity.ok(null);
            }
        }catch (UnknownAccountException e) {  //账号不存在
            logger.info("对用户进行登录验证..验证未通过,未知账户! username = {}", username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("用户名不存在!");
        } catch (IncorrectCredentialsException e) {
            logger.info("对用户进行登录验证..验证未通过,错误的凭证! username = {}", username);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (LockedAccountException e) {
            logger.info("对用户进行登录验证..验证未通过,账户已锁定! username = {}", username);
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(null);
        }catch(ExcessiveAttemptsException eae) {
            logger.info("对用户进行登录验证..验证未通过,错误次数过多! username = {}", username);
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(null);
        } catch (AuthenticationException e) {
            logger.info("对用户进行登录验证..验证未通过,身份验证失败! username = {}" ,username);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ttt");
            logger.error("对用户进行登录验证失败! username = {} e = {}", username, e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @PostMapping("/register")
    @ResponseBody
    public void register(User user){
        //try {
            userService.register(user);
       /* }catch (UsernameExistException e0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("用户名已经存在!");
        }
        catch (PhoneExistException e1){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("手机号码已经存在!");
        }
        catch (EmailExistException e2){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("邮箱已经存在!");
        }
        catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }*/

     //   return ResponseEntity.ok(null);
    }


}
