package com.example.mes.modules.system.service;

import java.util.List;

public interface SysRoleMenuService {

    void assignMenus(Long roleId, List<Long> menuIds);
}
