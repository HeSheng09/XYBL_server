package com.xybl.server.dao;

import com.xybl.server.ServerApplication;
import com.xybl.server.entity.Log;
import com.xybl.server.entity.User;
import com.xybl.server.utils.LogUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ServerApplication.class)
class LogDaoTest {
    @Resource
    private LogDao logDao;

    @Test
    void addOneLog() {
        User user=new User("920210202000001","测试学生",9);
        logDao.addOneLog(LogUtil.genLog(user,"插入日志测试。","succeed"));
    }

    @Test
    void getLogs() {
        List<Log> logs= logDao.getLogs();
        for(Log log:logs){
            System.out.println(log);
        }
    }
}