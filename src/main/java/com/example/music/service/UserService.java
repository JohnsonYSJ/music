package com.example.music.service;

import com.example.music.mapper.UserMapper;
import com.example.music.model.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName: UserService
 * @Description: TODO
 * @Author: JohnsonYSJ
 * @Date: 2019/9/9 1:52
 * @Version: 1.0
 */
@Service
public class UserService {

    @Resource
    private UserMapper mapper;

    public User login(String email, String password) {
        return mapper.login(email, password);
    }

}
