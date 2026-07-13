package com.example.mes.modules.erp.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.erp.entity.ErpSyncLog;

public interface ErpSyncLogService {

    ErpSyncLog createSyncLog(ErpSyncLog log);

    PageResult<ErpSyncLog> listSyncLogs(String keyword, String status, int page, int pageSize);
}
