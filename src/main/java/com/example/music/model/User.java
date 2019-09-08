package com.example.music.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @ClassName: User
 * @Description: 用户
 * @Author: JohnsonYSJ
 * @Date: 2019/9/7 1:03
 * @Version: 1.0
 */
@Setter
@Getter
public class User {

    /**
     * 邮箱
     */
    private String email;

    /**
     * 电话号码
     */
    private String phone;

    /**
    * 密码
    */
    private String password;

    /**
     * 验证码
     */
    private String verifyCode;

    /**
     * 名字
     */
    private String name;

    /**
     * 用户类型 0 代表管理员 1代表会员 2代表普通观众
     */
    private int type;

    /**
     * 是否可用
     */
    private boolean isEnabled;

    /**
     * 性别
     */
    private boolean sex;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
