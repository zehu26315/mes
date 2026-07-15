package com.example.mes.modules.system.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.config.CacheNames;
import com.example.mes.modules.system.entity.SysLoginLog;
import com.example.mes.modules.system.mapper.SysLoginLogMapper;
import com.example.mes.modules.system.service.SysLoginLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SysLoginLogServiceImpl implements SysLoginLogService {

    private final SysLoginLogMapper loginLogMapper;

    @Override
    @Transactional
    public SysLoginLog record(SysLoginLog log) {
        log.setCreateTime(LocalDateTime.now());
        loginLogMapper.insert(log);
        return log;
    }

    @Override
    @Cacheable(value = CacheNames.SYS_LOGIN_LOG, key = "#id", unless = "#result == null")
    public SysLoginLog getById(Long id) {
        return loginLogMapper.selectById(id);
    }

    @Override
    public PageResult<SysLoginLog> list(String loginName, Integer status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<SysLoginLog> rows = loginLogMapper.selectList(loginName, status, offset, pageSize);
        long total = loginLogMapper.countList(loginName, status);
        return PageResult.of(total, page, pageSize, rows);
    }

    @Override
    @Transactional
    @CacheEvict(value = CacheNames.SYS_LOGIN_LOG, allEntries = true)
    public void delete(Long id) {
        loginLogMapper.deleteById(id);
    }
}
