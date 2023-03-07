package com.timeworx.modules.security.proxy;

import com.timeworx.common.constant.ReturnCode;
import com.timeworx.common.entity.base.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

/**
 * @Description
 * @Author: ryzhang
 * @Date 2023/3/6 9:35 PM
 */
@Service
public class EmailProxy{

    private static final Logger logger = LoggerFactory.getLogger(EmailProxy.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.from:}")
    private String userEmail;

    public Response sendVerifyCode(String toMail, String code) {
        Response response = new Response(ReturnCode.SUCCESS, "success");
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toMail);
            helper.setSubject("Verification Code");
            helper.setText("Your verification code is: " + code);
            helper.setFrom(userEmail);
            javaMailSender.send(message);
        } catch (Exception e) {
            logger.error("email:{} send verify code exception:", toMail, e);
            response = new Response(ReturnCode.EXCEPTION, "send code error!");
        }

        return response;
    }
}
