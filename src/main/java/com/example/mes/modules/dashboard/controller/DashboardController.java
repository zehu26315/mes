package com.example.mes.modules.dashboard.controller;

import com.example.mes.common.response.Result;
import com.example.mes.modules.production.service.ProductionOrderService;
import com.example.mes.modules.quality.service.QualityInspectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final ProductionOrderService productionOrderService;
    private final QualityInspectionService qualityInspectionService;

    @GetMapping
    public Result<Map<String, Object>> overview() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("production", productionOrderService.dashboardStats());
        data.put("quality", qualityInspectionService.stats());
        return Result.ok(data);
    }
}
