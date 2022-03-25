package org.qingshan.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.qingshan.dao.entity.User;
import org.qingshan.dao.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestDao {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testDB() {
        User user = new User();
        user.setUsername("xixi");
        userMapper.insert(user);
        userMapper.updateByPrimaryKey(user);
    }
}
