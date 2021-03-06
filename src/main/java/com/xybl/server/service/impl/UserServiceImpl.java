package com.xybl.server.service.impl;

import com.xybl.server.dao.UserDao;
import com.xybl.server.entity.NsUser;
import com.xybl.server.entity.Student;
import com.xybl.server.entity.User;
import com.xybl.server.service.UserService;
import com.xybl.server.utils.IDUtil;
import com.xybl.server.utils.MD5Util;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.xybl.server.utils.ResponseUtil.response;

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
    public int addOneStu(Student stu) {
        int isAdd = 200;
        if (userDao.getUserByName(stu.getName()) != null) {
            isAdd = 401;//用户已存在
        } else {
            try{
                userDao.addOneUser(new User(stu.getId(), stu.getName(), stu.getPwd(), false));
                userDao.addOneStu(stu);
            }catch (Exception e){
                // 插库出错，删除插入的数据
                userDao.deleteOneStu(stu.getId());
                userDao.deleteOneUser(stu.getId());
                isAdd=500;//系统错误
            }
        }
        return isAdd;
    }

    @Override
    public Map<String, Object> login(String name, String pwd) {
        User user = userDao.getUserByName(name);
        if (user == null) {
            return response(400, "nouser"); //用户不存在
        } else if (MD5Util.validText(pwd, user.getPwd())) {
            return response(200, "ok", user); //密码正确
        } else {
            return response(401, "pwderror"); //密码错误
        }
    }

    @Override
    public String genId() {
        Map<String, Object> map = userDao.getLastUserId();
        int last_id = -1;
        if (map != null) {
            last_id = Integer.parseInt(map.get("last_id").toString());
        }
        return IDUtil.genId(last_id);
    }

    @Override
    public User getUserById(String id) {
        return userDao.getUserById(id);
    }

    @Override
    public Student getStuById(String id) {
        return userDao.getStuById(id);
    }

    @Override
    public User getUserByName(String name) {
        return userDao.getUserByName(name);
    }

    @Override
    public int addOneNsu(NsUser nsu) {
        int isAdd = 200;
        if(userDao.getUserById(nsu.getId()) != null){
            isAdd = 401; //用户已存在
        }else{
            try{
                userDao.addOneUser(new User(nsu.getId(), nsu.getName(), nsu.getPwd(), true));
                userDao.addOneNsu(nsu);
            }catch (Exception e){
                // 插库出错， 删除插入的数据
                userDao.delOneNsu(nsu.getId());
                userDao.deleteOneUser(nsu.getId());
                isAdd = 500;//系统错误
            }
        }
        return isAdd;
    }

    @Override
    public NsUser getNsUserById(String id) {
        return userDao.getNsUserById(id);
    }

    @Override
    public int delOneUserById(String id) {
        int isDel = 200;
        if(userDao.getUserById(id) != null){
            userDao.deleteOneUser(id);
            if(userDao.getUserById(id).getRole() == true){
                userDao.delOneNsu(id);
            }else{
                userDao.deleteOneStu(id);
            }
            return isDel; //成功删除
        }else{
            return 401;//用户不存在
        }
    }

    @Override
    public String genNsUserName(String authoName, String beNamed) {

        String result = "";

        String t = authoName.substring(0, 2);
        String j = authoName.substring(2, 4);
        String x = authoName.substring(4, 7);
        String s = authoName.substring(7, 10);
        String hasRight = authoName.substring(10, 11);

        if (!"0".equals(hasRight)) { //该账号没有分配账号的权限
            return result;
        }
        String searchPart = "0";
        String appendix = "0";
        if ("00".equals(t)) { //教育部0级账号授权省级账号
            searchPart = "1";
        } else if ("00".equals(j)) {//省级账号授权给市级账号
            searchPart = "2";
            result += t;
        } else if ("000".equals(x)) {//市级账号授权给县级账号
            searchPart = "3";
            result += (t + j);
            appendix = "00";
        } else if ("000".equals(s)) {//县级账号授权给学校账号
            searchPart = "4";
            result += (t + j + x);
            appendix = "00";
        }
        int lastNum = Integer.parseInt(userDao.getLastDmschNameNum(searchPart));
        if ((lastNum + 1) < 10) {
            result += appendix + String.valueOf(lastNum + 1);
        } else {
            result += String.valueOf(lastNum + 1);
        }
        return IDUtil.strBackFillZero(result, 13);
    }

    @Override
    public int addOneDmschName(String dmschName, String userName) {
        userDao.addOneDmschName(dmschName, userName);
        return 200;
    }

    @Override
    public String getDmschName(String nameCode) {
        return userDao.getDmschName(nameCode);
    }

    @Override
    public String genNsUserPerName(String authoName, String privilege) {
        String perName = "";
        String front = authoName.substring(0, 10);
        perName += front;
        perName += privilege;
        //get相同前11位的最新用户编号
        if(userDao.getLastNsNameNum(perName) == null){
            perName += "00";
        }else{
            int lastP = Integer.parseInt(userDao.getLastNsNameNum(perName));
            if( (lastP+1) < 10){
                perName += ("0" + String.valueOf(lastP+1) );
            }else{
                perName += String.valueOf(lastP+1);
            }
        }
        return perName;
    }

    @Override
    public String getSuperDmId(String ns_id) {
        String self_org_id=userDao.getUserById(ns_id).getName().substring(0,10);
        int self_org_index=0;
        for(int i=9;i>=0;i--){
            if('0'!=(self_org_id.charAt(i))){
                self_org_index=i;
                break;
            }
        }
        String super_org="";
        switch (self_org_index){
            case 9:
                super_org+=self_org_id.substring(0,7)+"000";
                break;
            case 6:
                super_org+=self_org_id.substring(0,4)+"000000";
                break;
            case 3:
                super_org+=self_org_id.substring(0,2)+"00000000";
                break;
            case 1:
                super_org+="0000000000";
                break;
        }
        return userDao.getUserByName(super_org+"000").getId();
    }

    @Override
    public String getSchIdByStuid(String stu_id) {
        return userDao.getSchIdByStuid(stu_id);
    }

    @Override
    public void updateStu(Student New) {
        userDao.updateStu(New);
    }

    @Override
    public void updateNs(NsUser New) {
        userDao.updateNs(New);
    }

    @Override
    public void updateUser(User New) {
        userDao.updateUser(New);
    }

    @Override
    public String getDmschNidByNsUid(String nsUid) {
        return userDao.getDmschNidByNsUid(nsUid);
    }

    @Override
    public String getUidByNid(String nid) {
        return userDao.getUidByNid(nid);
    }
}
