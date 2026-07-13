package com.example.mes.modules.system.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.common.response.TreeNode;
import com.example.mes.common.util.TreeUtils;
import com.example.mes.config.CacheNames;
import com.example.mes.modules.system.entity.SysMenu;
import com.example.mes.modules.system.mapper.SysMenuMapper;
import com.example.mes.modules.system.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl implements SysMenuService {

    private final SysMenuMapper sysMenuMapper;

    @Override
    @Transactional
    @CacheEvict(cacheNames = {CacheNames.SYS_MENU, CacheNames.SYS_MENU_TREE, CacheNames.SYS_MENU_LIST}, allEntries = true)
    public SysMenu createMenu(SysMenu menu) {
        if (menu.getStatus() == null) {
            menu.setStatus(1);
        }
        if (menu.getVisible() == null) {
            menu.setVisible(1);
        }
        sysMenuMapper.insert(menu);
        return menu;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {CacheNames.SYS_MENU, CacheNames.SYS_MENU_TREE, CacheNames.SYS_MENU_LIST}, allEntries = true)
    public SysMenu updateMenu(SysMenu menu) {
        menu.setMenuCode(null);
        sysMenuMapper.update(menu);
        return sysMenuMapper.selectById(menu.getId());
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {CacheNames.SYS_MENU, CacheNames.SYS_MENU_TREE, CacheNames.SYS_MENU_LIST}, allEntries = true)
    public void deleteMenu(Long id) {
        sysMenuMapper.deleteById(id);
    }

    @Override
    @Cacheable(value = CacheNames.SYS_MENU, key = "#id", unless = "#result == null")
    public SysMenu getMenuById(Long id) {
        return sysMenuMapper.selectById(id);
    }

    @Override
    public PageResult<SysMenu> listMenus(String keyword, Integer status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<SysMenu> rows = sysMenuMapper.selectList(keyword, status, offset, pageSize);
        long total = sysMenuMapper.countList(keyword, status);
        return PageResult.of(total, page, pageSize, rows);
    }

    @Override
    @Cacheable(value = CacheNames.SYS_MENU_TREE, unless = "#result == null || #result.isEmpty()")
    public List<TreeNode<SysMenu>> getMenuTree() {
        List<SysMenu> allMenus = sysMenuMapper.selectList(null, null, 0, Integer.MAX_VALUE);
        return TreeUtils.buildTree(allMenus, 0L,
                SysMenu::getId,
                m -> m.getParentId() == null ? 0L : m.getParentId());
    }
}
