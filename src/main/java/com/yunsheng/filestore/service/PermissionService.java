package com.yunsheng.filestore.service;

import com.yunsheng.filestore.entity.Role;

import java.util.Set;

public interface PermissionService {
    Set<String> getPermissions(String username);

}
