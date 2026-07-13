package com.example.mes.modules.system.controller;

import com.example.mes.common.response.PageResult;
import com.example.mes.common.response.Result;
import com.example.mes.modules.system.entity.SysOperationLog;
import com.example.mes.modules.system.service.SysOperationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sys/logs")
@RequiredArgsConstructor
public class SysOperationLogController {

    private final SysOperationLogService sysOperationLogService;

    @GetMapping
    public Result<PageResult<SysOperationLog>> list(@RequestParam(defaultValue = "1") int page,
                                                     @RequestParam(defaultValue = "20") int pageSize) {
        return Result.ok(sysOperationLogService.listLogs(page, pageSize));
    }
}
