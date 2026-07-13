package com.example.mes.modules.system.service.impl;

import com.example.mes.config.CacheNames;
import com.example.mes.modules.system.entity.SysRoleMenu;
import com.example.mes.modules.system.mapper.SysRoleMenuMapper;
import com.example.mes.modules.system.service.SysRoleMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysRoleMenuServiceImpl implements SysRoleMenuService {

    private final SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    @Transactional
    @CacheEvict(cacheNames = CacheNames.SYS_ROLE, key = "#roleId")
    public void assignMenus(Long roleId, List<Long> menuIds) {
        sysRoleMenuMapper.deleteByRoleId(roleId);
        if (menuIds != null && !menuIds.isEmpty()) {
            List<SysRoleMenu> roleMenus = menuIds.stream()
                    .map(menuId -> SysRoleMenu.builder()
                            .roleId(roleId)
                            .menuId(menuId)
                            .build())
                    .collect(Collectors.toList());
            sysRoleMenuMapper.batchInsert(roleMenus);
        }
    }
}
