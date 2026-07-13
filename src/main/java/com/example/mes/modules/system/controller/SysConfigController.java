package com.example.mes.modules.system.controller;

import com.example.mes.common.response.Result;
import com.example.mes.modules.system.entity.SysConfig;
import com.example.mes.modules.system.service.SysConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sys/configs")
@RequiredArgsConstructor
public class SysConfigController {

    private final SysConfigService sysConfigService;

    @PutMapping("/{id}")
    public Result<SysConfig> update(@PathVariable Long id, @RequestBody SysConfig config) {
        config.setId(id);
        return Result.ok(sysConfigService.updateConfig(config));
    }

    @GetMapping
    public Result<List<SysConfig>> listAll() {
        return Result.ok(sysConfigService.listAll());
    }

    @GetMapping("/{id}")
    public Result<SysConfig> getById(@PathVariable Long id) {
        return Result.ok(sysConfigService.getConfigById(id));
    }
}
