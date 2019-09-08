package com.example.music.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @ClassName: UserController
 * @Description: 用户controller
 * @Author: JohnsonYSJ
 * @Date: 2019/9/4 23:26
 * @Version: 1.0
 */
@Controller
@RequestMapping("/user")
public class UserController {

    //region Description
    @Resource
    private DefaultKaptcha captchaProducer;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    /**
     * @Author: JohnsonYSJ
     * @Description: 跳转到登陆页面
     * @Date: 2019/9/5 6:14
     * @Param: []
     * @return: java.lang.String
     **/
    @RequestMapping("/toLogin")
    public String login() {
        return "login";
    }

    /**
     * @Author: JohnsonYSJ
     * @Description: 获取验证码的请求路径
     * @Date: 2019/9/5 8:03
     * @Param: [httpServletRequest, httpServletResponse]
     * @return: void
     **/
    @RequestMapping("/defaultKaptcha")
    public void defaultKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        byte[] captchaChallengeAsJpeg;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            //生产验证码字符串并保存到session中
            String createText = captchaProducer.createText();
            httpServletRequest.getSession().setAttribute("verifyCode", createText);
            //使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage challenge = captchaProducer.createImage(createText);
            ImageIO.write(challenge, "jpg", jpegOutputStream);
        } catch (IOException e) {
            try {
                httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return;
        }
        //定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = null;
        try {
            responseOutputStream =
                    httpServletResponse.getOutputStream();
            responseOutputStream.write(captchaChallengeAsJpeg);
            responseOutputStream.flush();
            responseOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (responseOutputStream != null) {
                    responseOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping("/login")
    public String verify(HttpServletRequest httpServletRequest, Model model) {
        String captchaId = (String) httpServletRequest.getSession().getAttribute("verifyCode");
        String parameter = httpServletRequest.getParameter("j_captcha");
        LOGGER.info("Session  j_captcha " + captchaId + " form j_captcha " + parameter);
        if (!captchaId.equals(parameter)) {
            model.addAttribute("info", "错误的验证码");
            return "login";
        } else {
            model.addAttribute("info", "登录成功");
            return "index";
        }
    }
    //endregion

}
