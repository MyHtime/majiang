package cn.tecnpan.majiang.helloworld.service;

import cn.tecnpan.majiang.helloworld.model.User;

public interface UserService {

    void insertUser(User user);

    User findUserByToken(String token);

    void createOrUpdate(User user);
}
