package com.example.mes.modules.system.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.system.entity.SysOperationLog;
import com.example.mes.modules.system.mapper.SysOperationLogMapper;
import com.example.mes.modules.system.service.SysOperationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SysOperationLogServiceImpl implements SysOperationLogService {

    private final SysOperationLogMapper sysOperationLogMapper;

    @Override
    @Transactional
    public SysOperationLog createLog(SysOperationLog log) {
        log.setCreateTime(LocalDateTime.now());
        sysOperationLogMapper.insert(log);
        return log;
    }

    @Override
    public SysOperationLog getById(Long id) {
        return sysOperationLogMapper.selectById(id);
    }

    @Override
    public PageResult<SysOperationLog> listLogs(String operationName, String operationType, String operationIp,
                                                 String startTime, String endTime, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<SysOperationLog> rows = sysOperationLogMapper.selectList(
                operationName, operationType, operationIp, startTime, endTime, offset, pageSize);
        long total = sysOperationLogMapper.countList(
                operationName, operationType, operationIp, startTime, endTime);
        return PageResult.of(total, page, pageSize, rows);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        sysOperationLogMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        sysOperationLogMapper.deleteBatch(ids);
    }

    @Override
    public Map<String, Object> stats() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("totalRecords", sysOperationLogMapper.countTotal());
        result.put("todayOperations", sysOperationLogMapper.countToday());
        result.put("weekOperations", sysOperationLogMapper.countWeek());
        result.put("monthOperations", sysOperationLogMapper.countMonth());
        result.put("earliestRecord", sysOperationLogMapper.getEarliestTime());
        result.put("latestRecord", sysOperationLogMapper.getLatestTime());
        return result;
    }

    @Override
    public Map<String, Object> cleanSuggest(int keepDays) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("deletableCount", sysOperationLogMapper.countOlderThan(keepDays));
        result.put("days", keepDays);
        result.put("earliestDate", sysOperationLogMapper.getEarliestDate(keepDays));
        return result;
    }

    @Override
    @Transactional
    public long cleanByDays(int keepDays) {
        long count = sysOperationLogMapper.countOlderThan(keepDays);
        if (count > 0) {
            sysOperationLogMapper.deleteOlderThan(keepDays);
        }
        return count;
    }
}
