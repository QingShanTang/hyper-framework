package org.qingshan.web.service;

import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.qingshan.dao.entity.User;
import org.qingshan.dao.entity.UserExample;
import org.qingshan.dao.mapper.UserMapper;
import org.qingshan.web.pojo.UserPagingParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserService extends BaseService {

    @Autowired
    private UserMapper userMapper;

    public PageInfo<User> pagingUser(UserPagingParams params) {
        prePage(params);
        List<User> userList = userMapper.selectByExample(new UserExample());
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        return pageInfo;
    }
}
