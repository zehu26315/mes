package com.example.mes.modules.system.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.system.entity.SysRole;
import com.example.mes.modules.system.mapper.SysRoleMapper;
import com.example.mes.modules.system.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl implements SysRoleService {

    private final SysRoleMapper sysRoleMapper;

    @Override
    @Transactional
    public SysRole createRole(SysRole role) {
        if (role.getStatus() == null) {
            role.setStatus(1);
        }
        sysRoleMapper.insert(role);
        return role;
    }

    @Override
    @Transactional
    public SysRole updateRole(SysRole role) {
        role.setRoleCode(null); // 不允许修改角色编码
        sysRoleMapper.update(role);
        return sysRoleMapper.selectById(role.getId());
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        sysRoleMapper.deleteById(id);
    }

    @Override
    public SysRole getRoleById(Long id) {
        return sysRoleMapper.selectById(id);
    }

    @Override
    public List<SysRole> listAll() {
        return sysRoleMapper.selectAll();
    }

    @Override
    public PageResult<SysRole> listRoles(String keyword, Integer status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<SysRole> rows = sysRoleMapper.selectList(keyword, status, offset, pageSize);
        long total = sysRoleMapper.countList(keyword, status);
        return PageResult.of(total, page, pageSize, rows);
    }
}
