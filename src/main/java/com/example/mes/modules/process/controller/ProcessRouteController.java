package com.example.mes.modules.process.controller;

import com.example.mes.common.response.PageResult;
import com.example.mes.common.response.Result;
import com.example.mes.modules.process.entity.ProcessRoute;
import com.example.mes.modules.process.service.ProcessRouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/process/routes")
@RequiredArgsConstructor
public class ProcessRouteController {

    private final ProcessRouteService processRouteService;

    @PostMapping
    public Result<ProcessRoute> create(@RequestBody ProcessRoute route) {
        return Result.ok(processRouteService.createRoute(route));
    }

    @PutMapping("/{id}")
    public Result<ProcessRoute> update(@PathVariable Long id, @RequestBody ProcessRoute route) {
        route.setId(id);
        return Result.ok(processRouteService.updateRoute(route));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        processRouteService.deleteRoute(id);
        return Result.ok();
    }

    @GetMapping("/{id}")
    public Result<ProcessRoute> getById(@PathVariable Long id) {
        return Result.ok(processRouteService.getRouteById(id));
    }

    @GetMapping
    public Result<PageResult<ProcessRoute>> list(@RequestParam(defaultValue = "") String keyword,
                                                  @RequestParam(required = false) Integer status,
                                                  @RequestParam(defaultValue = "1") int page,
                                                  @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(processRouteService.listRoutes(keyword, status, page, pageSize));
    }
}
