package org.qingshan.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.qingshan.dao.entity.User;
import org.qingshan.dao.mapper.UserMapper;
import org.qingshan.dao.mapper.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestDao {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserDao userDao;

    @Test
    public void testBatchAdd() throws Exception {
        User xixi = new User();
        xixi.setUsername("xixi");
        xixi.setPassword("123");
        xixi.setIncome(1.1);
        xixi.setIfAdult(1);
        User haha = new User();
        haha.setUsername("haha");
        haha.setPassword("456");
        haha.setIncome(2.2);
        haha.setIfAdult(0);
        userDao.batchAdd(Arrays.asList(xixi, haha));
    }
}
