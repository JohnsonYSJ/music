package com.example.music.controller;

import com.example.music.model.User;
import com.example.music.service.UserService;
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

    @Resource
    private DefaultKaptcha captchaProducer;

    @Resource
    private UserService service;

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

    private boolean verify(HttpServletRequest request) {
        String captchaId = (String) request.getSession().getAttribute("verifyCode");
        String parameter = request.getParameter("j_captcha");
        LOGGER.info("Session  j_captcha " + captchaId + " form j_captcha " + parameter);
        return captchaId.equals(parameter);
    }

    @RequestMapping("/login")
    public String verify(HttpServletRequest request, Model model) {
        boolean flag = verify(request);
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        User user = service.login(email, password);
        if (user != null) {
            int type = user.getType();
            if (flag && type == 0) {
                model.addAttribute("name", user.getName());
                //如果是管理员
                return "/admin/index";
            } else if (flag && type == 1) {
                //如果是vip
                model.addAttribute("name", user.getName());
                return "index";
            } else if (flag && type == 2) {
                model.addAttribute("name", user.getName());
                //如果登录了
                return "index";
            } else {
                //游客
            }
        }
        return "index";
    }

}
