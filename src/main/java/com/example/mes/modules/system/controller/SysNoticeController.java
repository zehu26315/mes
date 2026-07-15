package com.example.mes.modules.system.controller;

import com.example.mes.common.response.PageResult;
import com.example.mes.common.response.Result;
import com.example.mes.modules.system.entity.SysNotice;
import com.example.mes.modules.system.service.SysNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sys/notice")
@RequiredArgsConstructor
public class SysNoticeController {

    private final SysNoticeService sysNoticeService;

    @PostMapping
    public Result<SysNotice> create(@RequestBody SysNotice notice) {
        return Result.ok(sysNoticeService.create(notice));
    }

    @PutMapping("/{id}")
    public Result<SysNotice> update(@PathVariable Long id, @RequestBody SysNotice notice) {
        notice.setId(id);
        return Result.ok(sysNoticeService.update(notice));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        sysNoticeService.delete(id);
        return Result.ok();
    }

    @GetMapping("/{id}")
    public Result<SysNotice> getById(@PathVariable Long id) {
        return Result.ok(sysNoticeService.getById(id));
    }

    @GetMapping
    public Result<PageResult<SysNotice>> list(@RequestParam(defaultValue = "") String keyword,
                                               @RequestParam(required = false) Integer noticeType,
                                               @RequestParam(required = false) Integer status,
                                               @RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(sysNoticeService.list(keyword, noticeType, status, page, pageSize));
    }
}
