package com.example.mes.modules.system.service.impl;

import com.example.mes.common.response.LoginVO;
import com.example.mes.common.response.PageResult;
import com.example.mes.config.CacheNames;
import com.example.mes.modules.system.entity.SysUser;
import com.example.mes.modules.system.mapper.SysUserMapper;
import com.example.mes.modules.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {

    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    private final SysUserMapper sysUserMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * 假 hash：用户名不存在时也对其执行 BCrypt matches，统一耗时以防时序侧信道枚举。
     * 用注入的 encoder 惰性生成一个与任何真实账户无关的随机常量口令的 hash，
     * 既保证是合法 BCrypt（matches 走完整耗时），又与 admin 的存储 hash 解耦。
     */
    private volatile String dummyHash;

    private String dummyHash() {
        String h = dummyHash;
        if (h == null) {
            synchronized (this) {
                h = dummyHash;
                if (h == null) {
                    h = passwordEncoder.encode("__mes_dummy_password_never_used__");
                    dummyHash = h;
                }
            }
        }
        return h;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = CacheNames.SYS_USER_LIST, allEntries = true)
    public SysUser createUser(SysUser user) {
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        SysUser exist = sysUserMapper.selectByUsername(user.getUsername());
        if (exist != null) {
            throw new IllegalArgumentException("用户名 '" + user.getUsername() + "' 已存在");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getStatus() == null) {
            user.setStatus(1);
        }
        sysUserMapper.insert(user);
        return user;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {CacheNames.SYS_USER, CacheNames.SYS_USER_LIST}, allEntries = true)
    public SysUser updateUser(SysUser user) {
        // 不允许修改用户名；密码为空则跳过，非空则重新加密
        user.setUsername(null);
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(null);
        }
        sysUserMapper.update(user);
        return sysUserMapper.selectById(user.getId());
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {CacheNames.SYS_USER, CacheNames.SYS_USER_LIST}, allEntries = true)
    public void deleteUser(Long id) {
        sysUserMapper.deleteById(id);
    }

    @Override
    @Cacheable(value = CacheNames.SYS_USER, key = "#id", unless = "#result == null")
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
    public LoginVO login(String username, String password) {
        SysUser user = sysUserMapper.selectByUsername(username);

        // 统一 BCrypt 耗时：用户不存在时也对固定 hash 执行 matches，防止时序侧信道枚举
        String storedHash = user != null ? user.getPassword() : dummyHash();
        boolean passwordMatch = passwordEncoder.matches(password, storedHash);

        if (user == null || !passwordMatch) {
            log.warn("Login failed for username '{}'", username);
            throw new IllegalArgumentException("用户名或密码错误");
        }
        if (user.getStatus() == null || user.getStatus() != 1) {
            log.warn("Login failed: user '{}' is disabled", username);
            throw new IllegalArgumentException("用户名或密码错误");
        }

        return LoginVO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .build();
    }
}
