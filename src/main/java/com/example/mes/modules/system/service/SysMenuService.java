package com.example.mes.modules.system.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.common.response.TreeNode;
import com.example.mes.modules.system.entity.SysMenu;

import java.util.List;

public interface SysMenuService {

    SysMenu createMenu(SysMenu menu);

    SysMenu updateMenu(SysMenu menu);

    void deleteMenu(Long id);

    SysMenu getMenuById(Long id);

    PageResult<SysMenu> listMenus(String keyword, Integer status, int page, int pageSize);

    List<TreeNode<SysMenu>> getMenuTree();
}
