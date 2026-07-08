package com.example.mes.modules.system.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.system.entity.SysUser;
import com.example.mes.modules.system.entity.SysUserRole;
import com.example.mes.modules.system.mapper.SysUserMapper;
import com.example.mes.modules.system.mapper.SysUserRoleMapper;
import com.example.mes.modules.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {

    private final SysUserMapper sysUserMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public SysUser createUser(SysUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getStatus() == null) {
            user.setStatus(1);
        }
        sysUserMapper.insert(user);
        return user;
    }

    @Override
    @Transactional
    public SysUser updateUser(SysUser user) {
        // 不允许修改密码和用户名
        user.setPassword(null);
        user.setUsername(null);
        sysUserMapper.update(user);
        return sysUserMapper.selectById(user.getId());
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        sysUserMapper.deleteById(id);
    }

    @Override
    public SysUser getUserById(Long id) {
        return sysUserMapper.selectById(id);
    }

    @Override
    public PageResult<SysUser> listUsers(String keyword, Integer status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<SysUser> rows = sysUserMapper.selectList(keyword, status, offset, pageSize);
        long total = sysUserMapper.countList(keyword, status);
        return PageResult.of(total, page, pageSize, rows);
    }

    @Override
    @Transactional
    public void assignRoles(Long userId, List<Long> roleIds) {
        sysUserRoleMapper.deleteByUserId(userId);
        if (roleIds != null && !roleIds.isEmpty()) {
            List<SysUserRole> userRoles = roleIds.stream()
                    .map(roleId -> SysUserRole.builder()
                            .userId(userId)
                            .roleId(roleId)
                            .build())
                    .collect(Collectors.toList());
            sysUserRoleMapper.batchInsert(userRoles);
        }
    }
}
