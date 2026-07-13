package com.example.mes.modules.system.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.system.entity.SysOperationLog;
import com.example.mes.modules.system.mapper.SysOperationLogMapper;
import com.example.mes.modules.system.service.SysOperationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysOperationLogServiceImpl implements SysOperationLogService {

    private final SysOperationLogMapper sysOperationLogMapper;

    @Override
    @Transactional
    public SysOperationLog createLog(SysOperationLog log) {
        sysOperationLogMapper.insert(log);
        return log;
    }

    @Override
    public PageResult<SysOperationLog> listLogs(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<SysOperationLog> rows = sysOperationLogMapper.selectList(offset, pageSize);
        long total = sysOperationLogMapper.countList();
        return PageResult.of(total, page, pageSize, rows);
    }
}
