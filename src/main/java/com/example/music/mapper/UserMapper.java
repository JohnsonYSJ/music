package com.example.music.mapper;

import com.example.music.model.User;

import java.util.List;

/**
 * @InterfaceName: UserMapper
 * @Description: 用户mapper
 * @Author: JohnsonYSJ
 * @Date: 2019/9/4 22:49
 * @Version: 1.0
 */
public interface UserMapper {

    /**
     * @Author: JohnsonYSJ
     * @Description: 获取所有的用户信息
     * @Date: 2019/9/7 1:11
     * @Param: []
     * @return: java.util.List<org.apache.catalina.User>
     **/
    List<User> list();

    /**
     * @Author: JohnsonYSJ
     * @Description: 登录
     * @Date: 2019/9/8 5:47
     * @Param: [email, password]
     * @return: void
     **/
    void login(String email, String password);

}
