package com.xybl.server.dao;

import com.xybl.server.ServerApplication;
import com.xybl.server.entity.School;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * SchoolDaoTest
 * <p></p>
 *
 * @author liubocai
 * @create 2021-03-08
 **/
@SpringBootTest(classes = ServerApplication.class)
class SchoolDaoTest {
    @Resource
    private SchoolDao schoolDao;

    @Test
    void getSchById(){
        School school = schoolDao.getSchoolById("0101001001000");
        System.out.println(school);
    }
}