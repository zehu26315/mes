package com.example.mes.modules.system.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.system.entity.SysLoginLog;

public interface SysLoginLogService {

    /** Record a login attempt (called internally). */
    SysLoginLog record(SysLoginLog log);

    SysLoginLog getById(Long id);

    PageResult<SysLoginLog> list(String loginName, Integer status, int page, int pageSize);

    void delete(Long id);
}
