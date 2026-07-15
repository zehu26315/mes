package com.example.mes.modules.system.controller;

import com.example.mes.common.response.PageResult;
import com.example.mes.common.response.Result;
import com.example.mes.modules.system.entity.SysLoginLog;
import com.example.mes.modules.system.service.SysLoginLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sys/login-log")
@RequiredArgsConstructor
public class SysLoginLogController {

    private final SysLoginLogService sysLoginLogService;

    @GetMapping
    public Result<PageResult<SysLoginLog>> list(@RequestParam(defaultValue = "") String loginName,
                                                 @RequestParam(required = false) Integer status,
                                                 @RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(sysLoginLogService.list(loginName, status, page, pageSize));
    }

    @GetMapping("/{id}")
    public Result<SysLoginLog> getById(@PathVariable Long id) {
        return Result.ok(sysLoginLogService.getById(id));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        sysLoginLogService.delete(id);
        return Result.ok();
    }
}
