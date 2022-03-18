package org.qingshan.dao.mapper;

import org.qingshan.dao.entity.User;

import java.util.List;

public interface UserMapper {
    List<User> findAll();
}
