package com.yunsheng.filestore.service;

import com.yunsheng.filestore.entity.Role;

import java.util.Set;

public interface RoleService {
    Set<String> getRoles(String username);

    int insertRole(Role role);
}
