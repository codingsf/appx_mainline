package top.appx.controller;

import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.oauth.Oauth;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import top.appx.exception.EmailExistException;
import top.appx.exception.MsgException;
import top.appx.service.MailService;
import top.appx.entity.Notify;
import top.appx.entity.User;
import top.appx.service.NotifyService;
import top.appx.service.UserService;
import top.appx.shiro.QQLoginToken;
import top.appx.zutil.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;

@Controller
public class LoginController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Autowired
    private NotifyService notifyService;

    @GetMapping("/login")
    public Object login(){
        return "/login";
    }



    @GetMapping("/qqlogin")
    public void qqLogin(HttpServletRequest request, HttpServletResponse response)throws Exception{
        String str = new Oauth().getAuthorizeURL(request);
        response.sendRedirect(str);
    }

    @GetMapping("/qqhl")
    public void qqhl(HttpServletResponse response,HttpServletRequest request,ModelMap modelMap)throws Exception{
        response.setContentType("text/html; charset=utf-8");

        AccessToken accessTokenObj = (new Oauth()).getAccessTokenByRequest(request);

        String accessToken   = null,
                openID        = null;
        long tokenExpireIn = 0L;


        if (accessTokenObj.getAccessToken().equals("")) {
//                我们的网站被CSRF攻击了或者用户取消了授权
//                做一些数据统计工作
            PrintWriter out = response.getWriter();

            out.print("出错了,请返回重新尝试登录");
        } else {
            accessToken = accessTokenObj.getAccessToken();
            tokenExpireIn = accessTokenObj.getExpireIn();

            request.getSession().setAttribute("demo_access_token", accessToken);
            request.getSession().setAttribute("demo_token_expirein", String.valueOf(tokenExpireIn));

            // 利用获取到的accessToken 去获取当前用的openid -------- start
            OpenID openIDObj = new OpenID(accessToken);
            openID = openIDObj.getUserOpenID();

            UserInfo qzoneUserInfo = new UserInfo(accessToken, openID);
            UserInfoBean userInfoBean = qzoneUserInfo.getUserInfo();
            String nickname = userInfoBean.getNickname();
            String img30 =userInfoBean.getAvatar().getAvatarURL30();
            User user1 = userService.findByQQOpenId(openID);
            if(user1==null){
                User user =new User();
                user.setQqOpenId(openID);
                user.setNickname(nickname);
                user.setAvatar(img30);
                request.getSession().setAttribute("qqUser",user);
                modelMap.put("qqUser",user);
                response.sendRedirect("/qqRegister");
            }else{
                QQLoginToken qqLoginToken = new QQLoginToken(user1.getUsername(),"123",true);
                login(qqLoginToken);
                response.sendRedirect("/");
            }

          //  login(user1.getUsername(),user1.getPassword());
        }
    }

    @GetMapping("/qqRegister")
    public String qqRegister(ModelMap modelMap,HttpSession session){
        modelMap.put("qqUser",session.getAttribute("qqUser"));
        return "/qqRegister";
    }

    @GetMapping("/register")
    public String register(){
        return "/register";
    }

    @GetMapping("/user/auth")
    @ResponseBody
    public void auth(@RequestParam("mail") String mail, HttpSession session)throws Exception{
        String checkCode =(int)((Math.random()*9+1)*1000)+"";
        if(userService.emailExist(mail)){
            throw new EmailExistException();
        }

        String msg ="您的验证码为:"+checkCode;
        Notify notify = new Notify();
        notify.setTarget(mail);
        notify.setType("email");
        notify.setTitle(msg);
        notify.setContent(msg);
        notifyService.save(notify);

        session.setAttribute("checkCode_"+mail,checkCode);
    }
    @PostMapping("/user/register")
    @ResponseBody
    public Object register(User userEntity,String checkCode,HttpSession session,@RequestParam("registerType") String registerType){


        if(session.getAttribute("inviteUserId")!=null){
            userEntity.setInviteUserId((Long)session.getAttribute("inviteUserId"));
        }else{
            userEntity.setInviteUserId(10L);//id为10的用户是机器人,默认奖励给机器人
        }

        System.out.println("注册中");

        if(StringUtil.isNullOrEmpty(userEntity.getUsername())){
            throw new MsgException(HttpStatus.BAD_REQUEST,"用户名不能为空");
        }
        if(StringUtil.isNullOrEmpty(userEntity.getPassword())){
            throw new MsgException(HttpStatus.BAD_REQUEST,"密码不能为空");
        }

        if("qq_new".equals(registerType) || "qq_old".equals(registerType)) {
            User qqUser = (User) session.getAttribute("qqUser");
            userEntity.setQqOpenId(qqUser.getQqOpenId());
            userEntity.setAvatar(qqUser.getAvatar());
        }
        if ("qq_old".equals(registerType)) {
            userService.registerByQQOld(userEntity);
        }else {

            if (StringUtil.isNullOrEmpty(userEntity.getEmail())) {
                throw new MsgException(HttpStatus.BAD_REQUEST, "邮箱不能为空");
            }
            Object checkCodeObj = session.getAttribute("checkCode_" + userEntity.getEmail());
            if (checkCodeObj == null) {
                throw new MsgException(HttpStatus.BAD_REQUEST, "没有生成验证码或验证码已经失效");
            }

            session.setAttribute("checkCode_" + userEntity.getEmail(), null);
            if (checkCodeObj != null && checkCodeObj.toString().equals(checkCode)) {

            } else {
                throw new MsgException(HttpStatus.BAD_REQUEST, "验证码错误");
            }
            userService.register(userEntity);
        }


        HashMap<String,Object> hashMap = new HashMap<String,Object>();
        hashMap.put("success",true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    mailService.sendSimpleMail("799378666@qq.com","新用户注册提醒","新用户注册,邮箱:"+userEntity.getEmail()+",邀请:"+userEntity.getInviteUserId());
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }).start();

        return hashMap;
    }


    private ResponseEntity login(UsernamePasswordToken token){
        Subject currentUser = SecurityUtils.getSubject();

        String username = token.getUsername();
        try {
            //在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
            //每个Realm都能在必要时对提交的AuthenticationTokens作出反应
            //所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
            //logger.info("对用户进行登录验证..验证开始! username = {}", username);
            long startTime = new Date().getTime();
            currentUser.login(token);
            long endtime = new Date().getTime();
            logger.info("耗时:"+(endtime-startTime));
            //验证是否登录成功
            if(currentUser.isAuthenticated()){
             //   logger.info("对用户进行登录验证..验证通过! username = {}", username);
                return ResponseEntity.ok(null);
            }
        }catch (UnknownAccountException e) {  //账号不存在
          // / logger.info("对用户进行登录验证..验证未通过,未知账户! username = {}", username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("用户名不存在!");
        } catch (IncorrectCredentialsException e) {
        //    logger.info("对用户进行登录验证..验证未通过,错误的凭证! username = {}", username);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户名或密码错误");
        } catch (LockedAccountException e) {
         //   logger.info("对用户进行登录验证..验证未通过,账户已锁定! username = {}", username);
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(null);
        }catch(ExcessiveAttemptsException eae) {
         //   logger.info("对用户进行登录验证..验证未通过,错误次数过多! username = {}", username);
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("验证失败,请稍后再试");
        } catch (AuthenticationException e) {
        //    logger.info("对用户进行登录验证..验证未通过,身份验证失败! username = {}" ,username);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ttt");
        //    logger.error("对用户进行登录验证失败! username = {} e = {}", username, e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }



    @PostMapping("/login")
    @ResponseBody
    public Object login(String username,String password) {
        //获取当前的Subject
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, true);

        return login(token);

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
