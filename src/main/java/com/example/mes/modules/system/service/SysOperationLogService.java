package com.example.mes.modules.system.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.system.entity.SysOperationLog;

import java.util.List;
import java.util.Map;

public interface SysOperationLogService {

    SysOperationLog createLog(SysOperationLog log);

    SysOperationLog getById(Long id);

    PageResult<SysOperationLog> listLogs(String operationName, String operationType, String operationIp,
                                         String startTime, String endTime, int page, int pageSize);

    void deleteById(Long id);

    void deleteBatch(List<Long> ids);

    Map<String, Object> stats();

    Map<String, Object> cleanSuggest(int keepDays);

    long cleanByDays(int keepDays);
}
