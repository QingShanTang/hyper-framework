package org.qingshan.dao.mapper.dao;

import org.apache.ibatis.annotations.Param;
import org.qingshan.dao.entity.User;

import java.util.List;

public interface UserDao {
    //mybatis批量操作数据
    void batchAdd(@Param("list") List<User> list);
}
