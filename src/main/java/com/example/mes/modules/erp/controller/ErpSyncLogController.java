package com.example.mes.modules.erp.controller;

import com.example.mes.common.response.PageResult;
import com.example.mes.common.response.Result;
import com.example.mes.modules.erp.entity.ErpSyncLog;
import com.example.mes.modules.erp.service.ErpSyncLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/erp")
@RequiredArgsConstructor
public class ErpSyncLogController {

    private final ErpSyncLogService erpSyncLogService;

    @GetMapping("/sync-logs")
    public Result<PageResult<ErpSyncLog>> list(@RequestParam(defaultValue = "") String keyword,
                                                @RequestParam(required = false) String status,
                                                @RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(erpSyncLogService.listSyncLogs(keyword, status, page, pageSize));
    }

    @PostMapping("/sync-logs")
    public Result<ErpSyncLog> create(@RequestBody ErpSyncLog log) {
        return Result.ok(erpSyncLogService.createSyncLog(log));
    }
}
