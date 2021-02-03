package com.xybl.server.service.impl;

import com.xybl.server.dao.UserDao;
import com.xybl.server.entity.User;
import com.xybl.server.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;

    @Override
    public int addOneUser(User user) {
        int isAdd = 200;
        if (userDao.getUserByName(user.getName()) != null) {
            isAdd = 401; //用户已存在
        } else {
            userDao.addOneUser(user);
        }
        return isAdd;
    }

    @Override
    public int login(String uid, String pwd) {
        User user = userDao.getUserById(uid);
        if (user == null) {
            return 402; //用户不存在
        } else if (pwd.equals(user.getPwd())) {
            return 200;
        } else {
            return 401; //密码错误
        }
    }

    @Override
    public int getLast_id() {
        Map<String, Object> map = userDao.getLastUserId();
        int last_id = 0;
        if (map != null) {
            last_id = Integer.parseInt(map.get("last_id").toString());
        }
        return last_id;
    }

    @Override
    public User getUserById(String id) {
        return userDao.getUserById(id);
    }
}
