package com.example.mes.modules.erp.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.erp.entity.ErpSyncLog;
import com.example.mes.modules.erp.mapper.ErpSyncLogMapper;
import com.example.mes.modules.erp.service.ErpSyncLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ErpSyncLogServiceImpl implements ErpSyncLogService {

    private final ErpSyncLogMapper erpSyncLogMapper;

    @Override
    @Transactional
    public ErpSyncLog createSyncLog(ErpSyncLog log) {
        erpSyncLogMapper.insert(log);
        return log;
    }

    @Override
    public PageResult<ErpSyncLog> listSyncLogs(String keyword, String status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<ErpSyncLog> rows = erpSyncLogMapper.selectList(keyword, status, offset, pageSize);
        long total = erpSyncLogMapper.countList(keyword, status);
        return PageResult.of(total, page, pageSize, rows);
    }
}
