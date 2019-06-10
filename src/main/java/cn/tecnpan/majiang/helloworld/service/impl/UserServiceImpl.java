package cn.tecnpan.majiang.helloworld.service.impl;

import cn.tecnpan.majiang.helloworld.mapper.UserMapper;
import cn.tecnpan.majiang.helloworld.model.User;
import cn.tecnpan.majiang.helloworld.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void insertUser(User user) {
        userMapper.insert(user);
    }
}
