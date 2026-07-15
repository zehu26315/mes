package com.example.mes.modules.system.controller;

import com.example.mes.common.response.PageResult;
import com.example.mes.common.response.Result;
import com.example.mes.modules.system.entity.SysOperationLog;
import com.example.mes.modules.system.service.SysOperationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sys/logs")
@RequiredArgsConstructor
public class SysOperationLogController {

    private final SysOperationLogService sysOperationLogService;

    @GetMapping
    public Result<PageResult<SysOperationLog>> list(@RequestParam(required = false) String operationName,
                                                     @RequestParam(required = false) String operationType,
                                                     @RequestParam(required = false) String operationIp,
                                                     @RequestParam(required = false) String startTime,
                                                     @RequestParam(required = false) String endTime,
                                                     @RequestParam(defaultValue = "1") int page,
                                                     @RequestParam(defaultValue = "20") int pageSize) {
        return Result.ok(sysOperationLogService.listLogs(
                operationName, operationType, operationIp, startTime, endTime, page, pageSize));
    }

    @GetMapping("/{id}")
    public Result<SysOperationLog> getById(@PathVariable Long id) {
        SysOperationLog log = sysOperationLogService.getById(id);
        if (log == null) {
            return Result.fail(404, "操作日志不存在");
        }
        return Result.ok(log);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        sysOperationLogService.deleteById(id);
        return Result.ok();
    }

    @DeleteMapping("/batch")
    public Result<Void> deleteBatch(@RequestBody List<Long> ids) {
        sysOperationLogService.deleteBatch(ids);
        return Result.ok();
    }

    @GetMapping("/stats")
    public Result<Map<String, Object>> stats() {
        return Result.ok(sysOperationLogService.stats());
    }

    @GetMapping("/clean-suggest")
    public Result<Map<String, Object>> cleanSuggest(@RequestParam(defaultValue = "90") int keepDays) {
        return Result.ok(sysOperationLogService.cleanSuggest(keepDays));
    }

    @DeleteMapping("/clean")
    public Result<Long> cleanByDays(@RequestParam(defaultValue = "90") int keepDays) {
        return Result.ok(sysOperationLogService.cleanByDays(keepDays));
    }
}
