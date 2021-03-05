package com.xybl.server.service;

import ch.qos.logback.core.joran.spi.NoAutoStartUtil;
import com.xybl.server.entity.NsUser;
import com.xybl.server.entity.Student;
import com.xybl.server.entity.User;

import java.util.Map;

/**
 * UserService
 * <p></p>
 * @author liubocai
 * @create 2021/2/2
 **/
public interface UserService {

   /**
   * addOneUser
   * <p>添加一名用户</p>
   * @param user com.xybl.server.entity.User.
   * @return int
   * @author liubocai
   * @create: 2021-02-03
   */
    public int addOneUser(User user);

    /**
    * login
    * <p>登录</p>
    * @param uid java.lang.String.
     * @param pwd java.lang.String.
    * @return int
    * @author liubocai
    * @create: 2021/2/2
    */
    public Map<String, Object> login(String uid, String pwd);

    /**
    * genId
    * <p>生成一个用户id。</p>
    * @param  .
    * @return java.lang.String
    * @author hesheng
    * @create: 2021/2/8
    */
    public String genId();

    /**
     * getUserById
     * <p>根据id获取用户对象</p>
     * @param id java.lang.String.
     * @return com.xybl.server.entity.User
     * @author liubocai
     * @create: 2021-02-03
     */
    public User getUserById(String id);


    public int addOneStu(Student stu);

    /**
    * addOneNsu
    * <p>添加一名非学生用户</p>
    * @param nsu com.xybl.server.entity.NsUser.
    * @return int
    * @author liubocai
    * @create: 2021-03-01
    */
    public int addOneNsu(NsUser nsu);

    public int delOneUserById(String id);
}
