package com.example.mes.modules.system.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.system.entity.SysOperationLog;

public interface SysOperationLogService {

    SysOperationLog createLog(SysOperationLog log);

    PageResult<SysOperationLog> listLogs(int page, int pageSize);
}
