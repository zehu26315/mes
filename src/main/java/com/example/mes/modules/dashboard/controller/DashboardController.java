package com.example.mes.modules.dashboard.controller;

import com.example.mes.common.response.Result;
import com.example.mes.modules.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Dashboard aggregation endpoints.
 * All endpoints return raw Map/List structures — no entities are defined
 * in this module since it purely aggregates data from other modules.
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public Result<Map<String, Object>> summary() {
        return Result.ok(dashboardService.getSummary());
    }

    @GetMapping("/recent-orders")
    public Result<List<Map<String, Object>>> recentOrders(
            @RequestParam(defaultValue = "10") int limit) {
        return Result.ok(dashboardService.getRecentWorkOrders(limit));
    }

    @GetMapping("/recent-alarms")
    public Result<List<Map<String, Object>>> recentAlarms(
            @RequestParam(defaultValue = "10") int limit) {
        return Result.ok(dashboardService.getRecentAlarms(limit));
    }

    @GetMapping("/line-status")
    public Result<List<Map<String, Object>>> lineStatus() {
        return Result.ok(dashboardService.getProductionLineStatus());
    }

    @GetMapping("/today-stats")
    public Result<Map<String, Object>> todayStats() {
        return Result.ok(dashboardService.getTodayStats());
    }

    @GetMapping("/equip-status")
    public Result<Map<String, Object>> equipStatus() {
        return Result.ok(dashboardService.getEquipmentStatusSummary());
    }

    @GetMapping("/energy-summary")
    public Result<Map<String, Object>> energySummary() {
        return Result.ok(dashboardService.getEnergySummary());
    }
}
