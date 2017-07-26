package top.appx.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public interface MailService {

    /**
     * 发送纯文本的简单邮件
     * @param to
     * @param subject
     * @param content
     */
    void sendSimpleMail(String to, String subject, String content) throws Exception;



    /**
     * 发送html格式的邮件
     * @param to
     * @param subject
     * @param content
     */
    void sendHtmlMail(String to, String subject, String content) throws Exception;


}
