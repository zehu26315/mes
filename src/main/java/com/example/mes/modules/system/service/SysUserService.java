package com.example.mes.modules.system.service;

import com.example.mes.common.response.LoginVO;
import com.example.mes.common.response.PageResult;
import com.example.mes.modules.system.entity.SysUser;

public interface SysUserService {

    SysUser createUser(SysUser user);

    SysUser updateUser(SysUser user);

    void deleteUser(Long id);

    SysUser getUserById(Long id);

    PageResult<SysUser> listUsers(String keyword, Integer status, int page, int pageSize);

    LoginVO login(String username, String password);
}
