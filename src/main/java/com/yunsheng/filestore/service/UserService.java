package com.yunsheng.filestore.service;

import com.yunsheng.filestore.entity.User;

public interface UserService {
    int insertUser(User one);

    User getUserByUsername(String username);
}
