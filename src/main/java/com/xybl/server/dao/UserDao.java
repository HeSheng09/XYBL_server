package com.xybl.server.dao;

import com.xybl.server.entity.User;

/**
 * UserDao
 * <p></p>
 * @author liubocai
 * @create 2021/2/2
 **/
public interface UserDao {

    /**
    * addOneUser
    * <p>添加一名用户</p>
    * @param user com.xybl.server.entity.User.
    * @return boolean
    * @author liubocai
    * @create: 2021/2/2
    */
    public boolean addOneUser(User user);

    /**
    * getUserById
    * <p>根据id查询用户</p>
    * @param uid java.lang.String.
    * @return com.xybl.server.entity.User
    * @author liubocai
    * @create: 2021/2/2
    */
    public User getUserById(String uid);

}