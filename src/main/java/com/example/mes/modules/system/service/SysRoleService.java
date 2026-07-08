package com.example.mes.modules.system.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.system.entity.SysRole;

import java.util.List;

public interface SysRoleService {

    SysRole createRole(SysRole role);

    SysRole updateRole(SysRole role);

    void deleteRole(Long id);

    SysRole getRoleById(Long id);

    List<SysRole> listAll();

    PageResult<SysRole> listRoles(String keyword, Integer status, int page, int pageSize);
}
